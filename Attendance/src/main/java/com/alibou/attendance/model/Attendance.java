package com.alibou.attendance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer studentId;
    private Integer sessionId;
    private LocalDateTime timestamp;
    private Boolean isPresent;
    private String verificationMethod; // "FACIAL", "MANUAL", etc.
    private Float confidenceScore; // For facial recognition confidence
}