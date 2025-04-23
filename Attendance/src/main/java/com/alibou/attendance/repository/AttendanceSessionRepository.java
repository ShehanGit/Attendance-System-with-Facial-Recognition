package com.alibou.attendance.repository;

import com.alibou.attendance.model.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Integer> {
    List<AttendanceSession> findBySchoolIdAndIsActiveTrue(Integer schoolId);
}