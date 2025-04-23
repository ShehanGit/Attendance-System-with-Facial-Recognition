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
public class SessionResponse {
    private Integer id;
    private Integer schoolId;
    private String className;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isActive;
}