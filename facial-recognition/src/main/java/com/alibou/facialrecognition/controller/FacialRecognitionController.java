package com.alibou.facialrecognition.controller;

import com.alibou.facialrecognition.dto.EnrollmentResponse;
import com.alibou.facialrecognition.dto.VerificationResponse;
import com.alibou.facialrecognition.service.FacialRecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/facial-recognition")
@RequiredArgsConstructor
public class FacialRecognitionController {

    private final FacialRecognitionService service;

    @PostMapping(value = "/enroll", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @RequestParam("studentId") Integer studentId,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(service.enrollStudent(studentId, image));
    }

    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VerificationResponse> verifyFace(
            @RequestParam("studentId") Integer studentId,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(service.verifyFace(image, studentId));
    }
}