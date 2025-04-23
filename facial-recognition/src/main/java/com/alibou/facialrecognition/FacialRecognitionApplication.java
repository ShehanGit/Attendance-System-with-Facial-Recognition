package com.alibou.facialrecognition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import nu.pattern.OpenCV;

@SpringBootApplication
public class FacialRecognitionApplication {

	public static void main(String[] args) {
		// Initialize OpenCV
		OpenCV.loadLocally();
		SpringApplication.run(FacialRecognitionApplication.class, args);
	}
}