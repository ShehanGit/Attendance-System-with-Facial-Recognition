package com.alibou.attendance.repository;

import com.alibou.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findBySessionId(Integer sessionId);
    Optional<Attendance> findBySessionIdAndStudentId(Integer sessionId, Integer studentId);
}