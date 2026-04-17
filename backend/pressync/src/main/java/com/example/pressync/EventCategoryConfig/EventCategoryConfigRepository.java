package com.example.pressync.EventCategoryConfig;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventCategoryConfigRepository extends JpaRepository<EventCategoryConfig,Integer> {
}
