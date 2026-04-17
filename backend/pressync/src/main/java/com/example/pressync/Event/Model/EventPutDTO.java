package com.example.pressync.Event.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPutDTO {
    private int id;
    private Event event;
}
