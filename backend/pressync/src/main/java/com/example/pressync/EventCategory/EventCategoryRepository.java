package com.example.pressync.EventCategory;

import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory,Integer> {
    @Query(value = "SELECT * FROM categorie_eveniment WHERE HOUR(starting_time) = :hour AND MINUTE(starting_time) = :minute",
            nativeQuery = true)
    List<EventCategory> findByStartingTimeNative(@Param("hour") int hour, @Param("minute") int minute);}
