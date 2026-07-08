package com.example.pressync.EventCategory;

import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {

    // 1. Added EntityGraph so the Native Query safely fetches the config!
    // 2. Removed the extra '}' at the end of the line.
    @EntityGraph(attributePaths = {"categoryConfig"})
    @Query(value = "SELECT * FROM categorii_evenimente WHERE HOUR(starting_time) = :hour AND MINUTE(starting_time) = :minute",
            nativeQuery = true)
    List<EventCategory> findByStartingTimeNative(@Param("hour") int hour, @Param("minute") int minute);

    @EntityGraph(attributePaths = {"categoryConfig", "createdBy"})
    @Query("SELECT c FROM EventCategory c LEFT JOIN FETCH c.categoryConfig LEFT JOIN FETCH c.createdBy")
    List<EventCategory> findAllWithConfigs();

    @Query("SELECT c FROM EventCategory c LEFT JOIN FETCH c.categoryConfig LEFT JOIN FETCH c.createdBy WHERE c.id = :id")
    Optional<EventCategory> findByIdWithDetails(@Param("id") Integer id);

    long countByCategoryConfig_Id(Integer configId);

    long countByCategoryConfig_IdAndIdNot(Integer configId, Integer categoryId);
}
