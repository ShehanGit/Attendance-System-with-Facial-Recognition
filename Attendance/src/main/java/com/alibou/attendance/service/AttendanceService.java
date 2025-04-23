package com.alibou.attendance.service;

import com.alibou.attendance.client.FacialRecognitionClient;
import com.alibou.attendance.client.StudentClient;
import com.alibou.attendance.dto.AttendanceRequest;
import com.alibou.attendance.dto.AttendanceResponse;
import com.alibou.attendance.dto.SessionRequest;
import com.alibou.attendance.dto.SessionResponse;
import com.alibou.attendance.model.Attendance;
import com.alibou.attendance.model.AttendanceSession;
import com.alibou.attendance.repository.AttendanceRepository;
import com.alibou.attendance.repository.AttendanceSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceSessionRepository sessionRepository;
    private final StudentClient studentClient;
    private final FacialRecognitionClient facialRecognitionClient;

    // Session Management
    public SessionResponse createSession(SessionRequest request) {
        var session = AttendanceSession.builder()
                .schoolId(request.getSchoolId())
                .className(request.getClassName())
                .subject(request.getSubject())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isActive(true)
                .build();

        sessionRepository.save(session);

        return mapToSessionResponse(session);
    }

    public SessionResponse closeSession(Integer sessionId) {
        var session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setIsActive(false);
        sessionRepository.save(session);

        return mapToSessionResponse(session);
    }

    public List<SessionResponse> getActiveSessions(Integer schoolId) {
        return sessionRepository.findBySchoolIdAndIsActiveTrue(schoolId).stream()
                .map(this::mapToSessionResponse)
                .collect(Collectors.toList());
    }

    // Attendance Management
    public AttendanceResponse markAttendance(AttendanceRequest request) {
        // Check if attendance already exists
        var existingAttendance = attendanceRepository.findBySessionIdAndStudentId(
                request.getSessionId(), request.getStudentId());

        if (existingAttendance.isPresent()) {
            return mapToAttendanceResponse(existingAttendance.get());
        }

        // Create new attendance record
        var attendance = Attendance.builder()
                .studentId(request.getStudentId())
                .sessionId(request.getSessionId())
                .timestamp(LocalDateTime.now())
                .isPresent(true)
                .verificationMethod(request.getVerificationMethod())
                .confidenceScore(1.0f) // Default for manual verification
                .build();

        attendanceRepository.save(attendance);

        return mapToAttendanceResponse(attendance);
    }

    public AttendanceResponse markAttendanceWithFacialRecognition(
            Integer studentId,
            Integer sessionId,
            MultipartFile image) {

        // Verify face using Facial Recognition Service
        var verificationResult = facialRecognitionClient.verifyFace(image, studentId);

        // Create attendance record based on verification result
        var attendance = Attendance.builder()
                .studentId(studentId)
                .sessionId(sessionId)
                .timestamp(LocalDateTime.now())
                .isPresent(verificationResult.matched())
                .verificationMethod("FACIAL")
                .confidenceScore(verificationResult.confidenceScore())
                .build();

        attendanceRepository.save(attendance);

        return mapToAttendanceResponse(attendance);
    }

    public List<AttendanceResponse> getSessionAttendance(Integer sessionId) {
        return attendanceRepository.findBySessionId(sessionId).stream()
                .map(this::mapToAttendanceResponse)
                .collect(Collectors.toList());
    }

    // Helper mapping methods
    private SessionResponse mapToSessionResponse(AttendanceSession session) {
        return SessionResponse.builder()
                .id(session.getId())
                .schoolId(session.getSchoolId())
                .className(session.getClassName())
                .subject(session.getSubject())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .isActive(session.getIsActive())
                .build();
    }

    private AttendanceResponse mapToAttendanceResponse(Attendance attendance) {
        // Fetch student info
        var student = studentClient.findStudentById(attendance.getStudentId());
        var session = sessionRepository.findById(attendance.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .studentId(attendance.getStudentId())
                .studentName(student.firstname() + " " + student.lastname())
                .sessionId(attendance.getSessionId())
                .sessionName(session.getClassName() + " - " + session.getSubject())
                .timestamp(attendance.getTimestamp())
                .isPresent(attendance.getIsPresent())
                .verificationMethod(attendance.getVerificationMethod())
                .build();
    }
}