package com.alibou.attendance.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", url = "${application.config.students-url}")
public interface StudentClient {

    @GetMapping("/{student-id}")
    StudentResponse findStudentById(@PathVariable("student-id") Integer studentId);

    // Response DTO
    record StudentResponse(
            Integer id,
            String firstname,
            String lastname,
            String email,
            Integer schoolId
    ) {}
}