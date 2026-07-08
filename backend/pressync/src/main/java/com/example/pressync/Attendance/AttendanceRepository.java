package com.example.pressync.Attendance;

import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.EventAttendanceSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    List<Attendance> findAllByUserId(Integer id);
    Boolean existsByUserIdAndEventId(Integer userId, Integer eventId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.event.eventCategory.id = :categoryId GROUP BY a.event.id ORDER BY a.event.date ASC, a.event.id ASC")
    List<Long> countAttendancePerEventByCategory(@Param("categoryId") Integer categoryId);

    @Query("SELECT NEW com.example.pressync.Attendance.Model.EventAttendanceSummary(a.event.date, COUNT(a)) FROM Attendance a WHERE a.event.eventCategory.id = :categoryId GROUP BY a.event.id, a.event.date ORDER BY a.event.date ASC, a.event.id ASC")
    List<EventAttendanceSummary> getEventAttendanceSummariesByCategory(@Param("categoryId") Integer categoryId);

    List<Attendance> findAllByUserEmail(String userEmail);

    List<Attendance> findAllByUserIdAndEventEventCategoryId(Integer integer, Integer integer1);

    @Query("SELECT a FROM Attendance a JOIN FETCH a.user JOIN FETCH a.event e JOIN FETCH e.eventCategory ec WHERE ec.id = :categoryId ORDER BY e.date ASC, a.joinedAt ASC")
    List<Attendance> findAllByEventCategoryIdWithDetails(@Param("categoryId") Integer categoryId);

    @Query("SELECT a FROM Attendance a JOIN FETCH a.user JOIN FETCH a.event e JOIN FETCH e.eventCategory ec WHERE ec.id = :categoryId AND a.user.id = :userId ORDER BY e.date ASC, a.joinedAt ASC")
    List<Attendance> findAllByUserIdAndEventCategoryIdWithDetails(@Param("userId") Integer userId, @Param("categoryId") Integer categoryId);
}
