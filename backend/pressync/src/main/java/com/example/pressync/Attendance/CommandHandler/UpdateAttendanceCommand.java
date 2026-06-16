package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceUpdateDTO;
import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAttendanceCommand implements Command<AttendanceUpdateDTO,String> {
    private final AttendanceRepository attendanceRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<String> execute(AttendanceUpdateDTO attendanceUpdateDTO) {
        int id = attendanceUpdateDTO.getId();
        Attendance existing = attendanceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Attendance with id " + id + " does not exist"));

        int userId = attendanceUpdateDTO.getAttendanceCreateDTO().getUserId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        int eventId = attendanceUpdateDTO.getAttendanceCreateDTO().getEventId();
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event does not exist"));

        if (Boolean.FALSE.equals(event.getActive())) {
            throw new IllegalArgumentException("Event is not active");
        }

        boolean exists = attendanceRepository.existsByUserIdAndEventId(userId, eventId);
        boolean sameUser = existing.getUser().getId().equals(userId);
        boolean sameEvent = existing.getEvent().getId().equals(eventId);
        if (exists && (!sameUser || !sameEvent)) {
            throw new IllegalArgumentException("This user is already marked for this event");
        }

        existing.setEvent(event);
        existing.setUser(user);
        attendanceRepository.save(existing);
        return ResponseEntity.ok("Successfully updated attendance");
    }
}
