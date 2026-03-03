package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceUpdateDTO;
import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateAttendanceCommand implements Command<AttendanceUpdateDTO,String> {
    private AttendanceRepository attendanceRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;

    public UpdateAttendanceCommand(AttendanceRepository attendanceRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<String> execute(AttendanceUpdateDTO attendanceUpdateDTO) {
        Attendance attendance = new Attendance();
        int user_id = attendanceUpdateDTO.getAttendanceCreateDTO().getUserId();
        int event_id= attendanceUpdateDTO.getAttendanceCreateDTO().getEventId();
        int id = attendanceUpdateDTO.getId();

        if(!attendanceRepository.existsById(id)){
            throw new IllegalArgumentException("Attendance with id " + id + " does not exist");
        }

        if (!userRepository.existsById(user_id)) {
            throw new IllegalArgumentException("User does not exist");
        }
        if (!eventRepository.existsById(event_id)) {
            throw new IllegalArgumentException("Event does not exist");
        }
        if (eventRepository.findById(event_id).get().getActive()==false) {
            throw new IllegalArgumentException("Event is not active");
        }
        attendance.setId(id);
        attendance.setEvent(eventRepository.findById(event_id).get());
        attendance.setUser(userRepository.findById(user_id).get());
        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Successfully updated attendance");
    }
}
