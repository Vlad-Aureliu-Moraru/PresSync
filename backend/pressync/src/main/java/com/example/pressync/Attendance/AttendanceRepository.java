package com.example.pressync.Attendance;

import com.example.pressync.Attendance.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    List<Attendance> findAllByUser(Integer id);
}
