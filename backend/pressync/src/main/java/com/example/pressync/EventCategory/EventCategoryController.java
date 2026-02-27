package com.example.pressync.EventCategory;

import com.example.pressync.EventCategory.CommandHandlers.CreateEventCategoryCommand;
import com.example.pressync.EventCategory.CommandHandlers.DeleteEventCategoryCommand;
import com.example.pressync.EventCategory.CommandHandlers.UpdateEventCategoryCommand;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryUpdateDTO;
import com.example.pressync.EventCategory.QueryHandlers.GetAllEventCategoriesQuerry;
import com.example.pressync.EventCategory.QueryHandlers.GetEventCategoryQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/EventCategory")
public class EventCategoryController {
    private CreateEventCategoryCommand createEventCategoryCommand;
    private GetAllEventCategoriesQuerry getAllEventCategoriesQuerry;
    private GetEventCategoryQuery  getEventCategoryQuery;
    private DeleteEventCategoryCommand deleteEventCategoryCommand;
    private UpdateEventCategoryCommand updateEventCategoryCommand;
    public EventCategoryController(DeleteEventCategoryCommand deleteEventCategoryCommand,UpdateEventCategoryCommand updateEventCategoryCommand,GetEventCategoryQuery getEventCategoryQuery,CreateEventCategoryCommand createEventCategoryCommand, GetAllEventCategoriesQuerry getAllEventCategoriesQuerry) {
        this.createEventCategoryCommand= createEventCategoryCommand;
        this.getAllEventCategoriesQuerry=getAllEventCategoriesQuerry;
        this.deleteEventCategoryCommand=deleteEventCategoryCommand;
        this.updateEventCategoryCommand=updateEventCategoryCommand;
        this.getEventCategoryQuery=getEventCategoryQuery;
    }


    @GetMapping
    public ResponseEntity<List<EventCategory>> getAllEventCategories(){
        return getAllEventCategoriesQuerry.execute(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCategory> getEventCategory(@PathVariable int id){
        return getEventCategoryQuery.execute(id);
    }
    @PostMapping()
    public ResponseEntity<String> addEventCategory(@RequestBody EventCategory eventCategory){
      return   createEventCategoryCommand.execute(eventCategory);

    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEventCategory(@PathVariable int id, @RequestBody EventCategory eventCategory){
        EventCategoryUpdateDTO eventCategoryUpdateDTO = new EventCategoryUpdateDTO(id,eventCategory);
        return updateEventCategoryCommand.execute(eventCategoryUpdateDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteEventCategory(@PathVariable int id){
        return deleteEventCategoryCommand.execute(id);
    }
}
