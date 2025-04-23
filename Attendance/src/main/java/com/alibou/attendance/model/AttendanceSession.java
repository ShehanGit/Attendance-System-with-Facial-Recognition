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
public class AttendanceSession {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer schoolId;
    private String className;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isActive;
}