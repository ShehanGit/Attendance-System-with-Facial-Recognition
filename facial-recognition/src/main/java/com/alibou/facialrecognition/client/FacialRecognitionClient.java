package com.alibou.facialrecognition.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "facial-recognition-service", url = "${application.config.facial-recognition-url}")
public interface FacialRecognitionClient {

    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    VerificationResponse verifyFace(
            @RequestPart("image") MultipartFile image,
            @RequestParam("studentId") Integer studentId
    );

    // Response DTO
    record VerificationResponse(
            boolean matched,
            float confidenceScore,
            Integer studentId,
            String message
    ) {}
}