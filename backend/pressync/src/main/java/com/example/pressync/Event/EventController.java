package com.example.pressync.Event;

import com.example.pressync.Event.CommandHandlers.CreateEventCommand;
import com.example.pressync.Event.CommandHandlers.DeleteEventCommand;
import com.example.pressync.Event.CommandHandlers.UpdateEventCommand;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventDTO;
import com.example.pressync.Event.QueryHandlers.GetAllEventsQuery;
import com.example.pressync.Event.QueryHandlers.GetEventByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final CreateEventCommand createEventCommand;
    private final DeleteEventCommand deleteEventCommand;
    private final UpdateEventCommand updateEventCommand;
    private final GetAllEventsQuery getAllEventsQuery;
    private final GetEventByIdQuery getEventByIdQuery;


    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        return getAllEventsQuery.execute(null);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer id){
        return  getEventByIdQuery.execute(id);

    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody Event event){
        return createEventCommand.execute(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEvent(@PathVariable Integer id){
        return deleteEventCommand.execute(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id,@RequestBody Event event){
        EventDTO eventDTO = new EventDTO(id, event);
        updateEventCommand.execute(eventDTO);
        return ResponseEntity.ok().build();
    }

}
