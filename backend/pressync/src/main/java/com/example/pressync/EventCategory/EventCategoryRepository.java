package com.example.pressync.EventCategory;

import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface EventCategoryRepository extends JpaRepository<EventCategory,Integer> {

}
