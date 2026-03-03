package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceCreateDTO;
import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class CreateAttendanceCommand implements Command<AttendanceCreateDTO,String> {
    private AttendanceRepository attendanceRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    public CreateAttendanceCommand(EventRepository eventRepository,UserRepository userRepository,AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }
    @Override
    public ResponseEntity execute(AttendanceCreateDTO  attendanceCreateDTO) {
        Attendance attendance = new Attendance();
        int user_id = attendanceCreateDTO.getUserId();
        int event_id= attendanceCreateDTO.getEventId();
        if(!userRepository.existsById(user_id)){
            throw new IllegalArgumentException("User does not exist");
        }
        if(!eventRepository.existsById(event_id)){
            throw new IllegalArgumentException("Event does not exist");
        }

        User user = userRepository.findById(user_id).get();
        Event event = eventRepository.findById(event_id).get();


        attendance.setUser(user);
        attendance.setEvent(event);
        attendanceRepository.save(attendance);
        return ResponseEntity.ok().build();
    }
}
