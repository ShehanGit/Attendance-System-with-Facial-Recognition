package com.alibou.facialrecognition.repository;

import com.alibou.facialrecognition.model.FaceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaceDataRepository extends JpaRepository<FaceData, Integer> {
    List<FaceData> findByStudentId(Integer studentId);
    Optional<FaceData> findTopByStudentIdOrderByVersionDesc(Integer studentId);
}