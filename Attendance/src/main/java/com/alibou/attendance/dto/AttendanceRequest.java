package com.alibou.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRequest {
    private Integer studentId;
    private Integer sessionId;
    private String verificationMethod; // "FACIAL", "MANUAL"
}