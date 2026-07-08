package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryChangedEvent;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteEventCategoryCommand implements Command<Integer, String> {
    private final EventCategoryRepository eventCategoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public ResponseEntity<String> execute(Integer entity) {
        EventCategory found = eventCategoryRepository.findById(entity)
                .orElseThrow(() -> new IllegalArgumentException("Event category with id " + entity + " does not exist."));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = currentUser.getRole() == UserRoles.ADMIN;
        boolean isOwner = found.getCreatedBy() != null && found.getCreatedBy().getId().equals(currentUser.getId());
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Only the creator or an admin can delete this category.");
        }

        eventCategoryRepository.delete(found);
        applicationEventPublisher.publishEvent(new EventCategoryChangedEvent(found));
        return ResponseEntity.ok().build();
    }
}
