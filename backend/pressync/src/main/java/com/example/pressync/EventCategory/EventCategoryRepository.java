package com.example.pressync.EventCategory;

import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {

    // 1. Added EntityGraph so the Native Query safely fetches the config!
    // 2. Removed the extra '}' at the end of the line.
    @EntityGraph(attributePaths = {"categoryConfig"})
    @Query(value = "SELECT * FROM categorie_eveniment WHERE HOUR(starting_time) = :hour AND MINUTE(starting_time) = :minute",
            nativeQuery = true)
    List<EventCategory> findByStartingTimeNative(@Param("hour") int hour, @Param("minute") int minute);

    // Our perfectly optimized JPQL query
    @Query("SELECT c FROM EventCategory c JOIN FETCH c.categoryConfig")
    List<EventCategory> findAllWithConfigs();
}