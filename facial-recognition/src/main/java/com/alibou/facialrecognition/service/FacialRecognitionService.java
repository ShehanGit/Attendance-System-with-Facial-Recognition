package com.alibou.facialrecognition.service;

import com.alibou.facialrecognition.dto.EnrollmentResponse;
import com.alibou.facialrecognition.dto.VerificationResponse;
import com.alibou.facialrecognition.model.FaceData;
import com.alibou.facialrecognition.repository.FaceDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacialRecognitionService {

    private final FaceDataRepository faceDataRepository;

    @Value("${application.config.face-data-path}")
    private String faceDataPath;

    // Face detection cascade classifier
    private final CascadeClassifier faceDetector = new CascadeClassifier();

    // Initialize the face detector
    public void init() {
        // Load face detection model
        // Note: In a real implementation, you'd need to provide the actual path to the OpenCV haarcascade file
        String classifierPath = "/path/to/haarcascade_frontalface_default.xml";
        faceDetector.load(classifierPath);

        // Create face data directory if it doesn't exist
        File directory = new File(faceDataPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public EnrollmentResponse enrollStudent(Integer studentId, MultipartFile image) {
        try {
            // Convert MultipartFile to Mat
            byte[] imageBytes = image.getBytes();
            Mat imageMat = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

            // Detect face
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(imageMat, faceDetections);
            Rect[] facesArray = faceDetections.toArray();

            if (facesArray.length == 0) {
                return EnrollmentResponse.builder()
                        .studentId(studentId)
                        .success(false)
                        .message("No face detected in the image")
                        .build();
            }

            if (facesArray.length > 1) {
                return EnrollmentResponse.builder()
                        .studentId(studentId)
                        .success(false)
                        .message("Multiple faces detected. Please provide an image with only one face")
                        .build();
            }

            // Extract face region
            Rect faceRect = facesArray[0];
            Mat face = new Mat(imageMat, faceRect);

            // Resize face to standard size (for consistency)
            Mat resizedFace = new Mat();
            Imgproc.resize(face, resizedFace, new org.opencv.core.Size(150, 150));

            // Convert face to grayscale (for feature extraction)
            Mat grayFace = new Mat();
            Imgproc.cvtColor(resizedFace, grayFace, Imgproc.COLOR_BGR2GRAY);

            // In a real application, you would extract facial features/embeddings here
            // For simplicity, we'll just use the raw pixel data
            byte[] faceEncoding = new byte[(int) (grayFace.total() * grayFace.elemSize())];
            grayFace.get(0, 0, faceEncoding);

            // Save the original image
            String filename = UUID.randomUUID().toString() + ".jpg";
            String filePath = faceDataPath + "/" + filename;
            Imgcodecs.imwrite(filePath, imageMat);

            // Get the latest version for this student
            Optional<FaceData> latestFaceData = faceDataRepository.findTopByStudentIdOrderByVersionDesc(studentId);
            int version = latestFaceData.map(FaceData::getVersion).orElse(0) + 1;

            // Save face data to database
            FaceData faceData = FaceData.builder()
                    .studentId(studentId)
                    .imagePath(filePath)
                    .faceEncoding(faceEncoding)
                    .createdAt(System.currentTimeMillis())
                    .version(version)
                    .build();

            faceDataRepository.save(faceData);

            return EnrollmentResponse.builder()
                    .id(faceData.getId())
                    .studentId(studentId)
                    .success(true)
                    .message("Face enrolled successfully")
                    .build();

        } catch (IOException e) {
            log.error("Error processing image", e);
            return EnrollmentResponse.builder()
                    .studentId(studentId)
                    .success(false)
                    .message("Error processing image: " + e.getMessage())
                    .build();
        }
    }

    public VerificationResponse verifyFace(MultipartFile image, Integer studentId) {
        try {
            // Get the latest face data for the student
            Optional<FaceData> storedFaceData = faceDataRepository.findTopByStudentIdOrderByVersionDesc(studentId);

            if (storedFaceData.isEmpty()) {
                return VerificationResponse.builder()
                        .matched(false)
                        .confidenceScore(0.0f)
                        .studentId(studentId)
                        .build();
            }

            // Convert input image to Mat
            byte[] imageBytes = image.getBytes();
            Mat imageMat = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

            // Detect face
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(imageMat, faceDetections);
            Rect[] facesArray = faceDetections.toArray();

            if (facesArray.length == 0) {
                return VerificationResponse.builder()
                        .matched(false)
                        .confidenceScore(0.0f)
                        .studentId(studentId)
                        .build();
            }

            // Extract and process face (similar to enrollment)
            Rect faceRect = facesArray[0];
            Mat face = new Mat(imageMat, faceRect);
            Mat resizedFace = new Mat();
            Imgproc.resize(face, resizedFace, new org.opencv.core.Size(150, 150));
            Mat grayFace = new Mat();
            Imgproc.cvtColor(resizedFace, grayFace, Imgproc.COLOR_BGR2GRAY);

            byte[] faceEncoding = new byte[(int) (grayFace.total() * grayFace.elemSize())];
            grayFace.get(0, 0, faceEncoding);

            // Compare face encodings
            // In a real application, you would use a proper face comparison algorithm
            // For simplicity, we'll use a basic similarity measure
            byte[] storedEncoding = storedFaceData.get().getFaceEncoding();
            float similarity = calculateSimilarity(faceEncoding, storedEncoding);

            // Threshold for matching
            boolean matched = similarity > 0.7f;

            return VerificationResponse.builder()
                    .matched(matched)
                    .confidenceScore(similarity)
                    .studentId(studentId)
                    .build();

        } catch (IOException e) {
            log.error("Error processing image", e);
            return VerificationResponse.builder()
                    .matched(false)
                    .confidenceScore(0.0f)
                    .studentId(studentId)
                    .build();
        }
    }

    // Simple similarity calculation (correlation-based)
    private float calculateSimilarity(byte[] encoding1, byte[] encoding2) {
        // Ensure same length
        if (encoding1.length != encoding2.length) {
            return 0.0f;
        }

        // Calculate normalized correlation
        float sum = 0;
        float sumSq1 = 0;
        float sumSq2 = 0;

        for (int i = 0; i < encoding1.length; i++) {
            int val1 = encoding1[i] & 0xFF;  // Convert to unsigned
            int val2 = encoding2[i] & 0xFF;
            sum += val1 * val2;
            sumSq1 += val1 * val1;
            sumSq2 += val2 * val2;
        }

        return (float) (sum / Math.sqrt(sumSq1 * sumSq2));
    }
}