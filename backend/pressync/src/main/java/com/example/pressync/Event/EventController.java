package com.example.pressync.Event;

import com.example.pressync.Event.CommandHandlers.CreateEventCommand;
import com.example.pressync.Event.CommandHandlers.DeleteEventCommand;
import com.example.pressync.Event.CommandHandlers.UpdateEventCommand;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventDTO;
import com.example.pressync.Event.QueryHandlers.GetAllEventsQuery;
import com.example.pressync.Event.QueryHandlers.GetEventByIdQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Event")
public class EventController {
    private CreateEventCommand createEventCommand;
    private DeleteEventCommand deleteEventCommand;
    private UpdateEventCommand updateEventCommand;
    private GetAllEventsQuery getAllEventsQuery;
    private GetEventByIdQuery getEventByIdQuery;

    public EventController(GetEventByIdQuery getEventByIdQuery,CreateEventCommand createEventCommand, DeleteEventCommand deleteEventCommand, UpdateEventCommand updateEventCommand, GetAllEventsQuery getAllEventsQuery) {
        this.createEventCommand = createEventCommand;
        this.deleteEventCommand = deleteEventCommand;
        this.updateEventCommand = updateEventCommand;
        this.getAllEventsQuery= getAllEventsQuery;
        this.getEventByIdQuery = getEventByIdQuery;
    }

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
