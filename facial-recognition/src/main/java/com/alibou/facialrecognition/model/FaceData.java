package com.alibou.facialrecognition.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaceData {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer studentId;

    // Store the file path to the image
    private String imagePath;

    // Store the face encoding as byte array
    @Lob
    @Column(length = 10000)
    private byte[] faceEncoding;

    // Timestamp and versioning
    private Long createdAt;
    private Integer version;
}