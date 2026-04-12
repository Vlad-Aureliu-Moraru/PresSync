package com.example.pressync.EventCategory.Model;

import com.example.pressync.EventCategory.Model.DTO.UpdateEventCategoryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryUpdateDTO {
    int id;
    UpdateEventCategoryRequest request;
}
