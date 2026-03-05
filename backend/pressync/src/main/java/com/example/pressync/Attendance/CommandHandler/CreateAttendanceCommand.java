package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceCreateDTO;
import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class CreateAttendanceCommand implements Command<Integer,String> {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity execute(Integer id) {
        Attendance attendance = new Attendance();
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findFirstByActiveTrue().orElseThrow(()-> new IllegalArgumentException("Event not found"));
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        if (attendanceRepository.existsByUserIdAndEventId(user.getId(), event.getId())) {
            throw new IllegalArgumentException("Attendance already exists");
        }

        LocalTime startWindow = event.getEventCategory().getAttendanceTimeStart().toLocalTime();
        int duration = event.getEventCategory().getAttendanceDuration().intValue();
        LocalTime endWindow = startWindow.plusMinutes(duration);

        if (now.isAfter(endWindow)||now.isBefore(startWindow)) {
            throw new IllegalArgumentException("Attendance window condition is not met");

        }

        attendance.setUser(user);
        attendance.setEvent(event);
        attendanceRepository.save(attendance);
        return ResponseEntity.ok().body("Succesfully joined attendance");
    }
}
