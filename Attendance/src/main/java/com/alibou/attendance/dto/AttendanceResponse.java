package com.alibou.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponse {
    private Integer id;
    private Integer studentId;
    private String studentName;
    private Integer sessionId;
    private String sessionName;
    private LocalDateTime timestamp;
    private Boolean isPresent;
    private String verificationMethod;
}