package com.example.pressync.EventCategory;

import com.example.pressync.EventCategory.CommandHandlers.CreateEventCategoryCommand;
import com.example.pressync.EventCategory.CommandHandlers.DeleteEventCategoryCommand;
import com.example.pressync.EventCategory.CommandHandlers.UpdateEventCategoryCommand;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.DTO.CreateEventCategoryRequest;
import com.example.pressync.EventCategory.Model.DTO.UpdateEventCategoryRequest;
import com.example.pressync.EventCategory.Model.EventCategoryUpdateDTO;
import com.example.pressync.EventCategory.QueryHandlers.GetAllEventCategoriesQuerry;
import com.example.pressync.EventCategory.QueryHandlers.GetEventCategoryDueTodayQuery;
import com.example.pressync.EventCategory.QueryHandlers.GetEventCategoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/eventCategory")
@RequiredArgsConstructor
public class EventCategoryController {
    private final CreateEventCategoryCommand createEventCategoryCommand;
    private final GetAllEventCategoriesQuerry getAllEventCategoriesQuerry;
    private final GetEventCategoryQuery  getEventCategoryQuery;
    private final DeleteEventCategoryCommand deleteEventCategoryCommand;
    private final UpdateEventCategoryCommand updateEventCategoryCommand;
    private final GetEventCategoryDueTodayQuery getEventCategoryDueTodayQuery;


    @GetMapping
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN')")
    public ResponseEntity<List<EventCategory>> getAllEventCategories(){
        return getAllEventCategoriesQuerry.execute(null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN')")
    public ResponseEntity<EventCategory> getEventCategory(@PathVariable int id){
        return getEventCategoryQuery.execute(id);
    }

    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN')")
    public ResponseEntity<List<EventCategory>> getEventCategoriesDueToday(){
        return getEventCategoryDueTodayQuery.execute(null);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addEventCategory(@RequestBody CreateEventCategoryRequest eventCategoryRequest){
      return   createEventCategoryCommand.execute(eventCategoryRequest);



    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateEventCategory(@PathVariable int id, @RequestBody UpdateEventCategoryRequest request){
        EventCategoryUpdateDTO eventCategoryUpdateDTO = new EventCategoryUpdateDTO(id, request);
        return updateEventCategoryCommand.execute(eventCategoryUpdateDTO);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpResponse> deleteEventCategory(@PathVariable int id){
        return deleteEventCategoryCommand.execute(id);
    }
}
