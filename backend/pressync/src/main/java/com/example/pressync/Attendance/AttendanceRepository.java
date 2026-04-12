package com.example.pressync.Attendance;

import com.example.pressync.Attendance.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    List<Attendance> findAllByUserId(Integer id);
    Boolean existsByUserIdAndEventId(Integer userId, Integer eventId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.event.eventCategory.id = :categoryId GROUP BY a.event.id ORDER BY a.event.date ASC")
    List<Long> countAttendancePerEventByCategory(@Param("categoryId") Integer categoryId);
}
