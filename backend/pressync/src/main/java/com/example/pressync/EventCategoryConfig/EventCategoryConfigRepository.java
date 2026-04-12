package com.example.pressync.EventCategoryConfig;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCategoryConfigRepository extends JpaRepository<EventCategoryConfig,Integer> {
}
