package com.alibou.attendance.controller;

import com.alibou.attendance.dto.AttendanceRequest;
import com.alibou.attendance.dto.AttendanceResponse;
import com.alibou.attendance.dto.SessionRequest;
import com.alibou.attendance.dto.SessionResponse;
import com.alibou.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    // Session endpoints
    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SessionResponse> createSession(@RequestBody SessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSession(request));
    }

    @PutMapping("/sessions/{session-id}/close")
    public ResponseEntity<SessionResponse> closeSession(@PathVariable("session-id") Integer sessionId) {
        return ResponseEntity.ok(service.closeSession(sessionId));
    }

    @GetMapping("/sessions/active/school/{school-id}")
    public ResponseEntity<List<SessionResponse>> getActiveSessions(@PathVariable("school-id") Integer schoolId) {
        return ResponseEntity.ok(service.getActiveSessions(schoolId));
    }

    // Attendance endpoints
    @PostMapping
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody AttendanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.markAttendance(request));
    }

    @PostMapping(value = "/facial-recognition", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttendanceResponse> markAttendanceWithFacialRecognition(
            @RequestParam("studentId") Integer studentId,
            @RequestParam("sessionId") Integer sessionId,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.markAttendanceWithFacialRecognition(studentId, sessionId, image));
    }

    @GetMapping("/sessions/{session-id}")
    public ResponseEntity<List<AttendanceResponse>> getSessionAttendance(
            @PathVariable("session-id") Integer sessionId) {
        return ResponseEntity.ok(service.getSessionAttendance(sessionId));
    }
}