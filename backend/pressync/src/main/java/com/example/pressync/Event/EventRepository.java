package com.example.pressync.Event;

import com.example.pressync.Event.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Integer> {
    Optional<Event> findFirstByActiveTrue();

    @Modifying
    @Query("UPDATE Event e SET e.active=false ")
    void deactivateAllEvents();

    @Query(value = "SELECT e.* FROM event e " +
            "JOIN categorie_eveniment ec ON e.event_category_id = ec.id " +
            "WHERE e.active = true " +
            "AND HOUR(ec.end_time) = :hour AND MINUTE(ec.end_time) = :minute",
            nativeQuery = true)
    List<Event> findExpiredEvents(@Param("hour") int hour, @Param("minute") int minute);

    @Modifying
    @Query("UPDATE Event e set e.active=false where e.eventCategory.id=:catId AND e.active=true")
    void deactivateByCategory(Integer catId);

    @Modifying
    @Query("UPDATE Event e set e.archived=true,e.active=false where e.eventCategory.id=:catId AND e.active=true")
    void archiveByCategory(Integer catId);

}