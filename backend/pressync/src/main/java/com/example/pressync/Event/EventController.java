package com.example.pressync.Event;

import com.example.pressync.Event.CommandHandlers.CreateEventCommand;
import com.example.pressync.Event.CommandHandlers.DeleteEventCommand;
import com.example.pressync.Event.CommandHandlers.UpdateEventCommand;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventGetDTO;
import com.example.pressync.Event.Model.EventPutDTO;
import com.example.pressync.Event.QueryHandlers.GetAllEventsQuery;
import com.example.pressync.Event.QueryHandlers.GetEventByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN')")
    public ResponseEntity<List<EventGetDTO>> getAllEvents(){
        return getAllEventsQuery.execute(null);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN')")
    public ResponseEntity<EventGetDTO> getEventById(@PathVariable Integer id){
        return  getEventByIdQuery.execute(id);

    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<String> createEvent(@RequestBody Event event){
        return createEventCommand.execute(event);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity deleteEvent(@PathVariable Integer id){
        return deleteEventCommand.execute(id);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<Event> updateEvent(@PathVariable int id,@RequestBody Event event){
        EventPutDTO eventPutDTO = new EventPutDTO(id, event);
        updateEventCommand.execute(eventPutDTO);
        return ResponseEntity.ok().build();
    }

}
