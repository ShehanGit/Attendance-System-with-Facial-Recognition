package com.alibou.facialrecognition.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationResult {
    private boolean matched;
    private float confidenceScore;
    private Integer studentId;
}