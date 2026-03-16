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
public class CreateAttendanceCommand implements Command<User,String> {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity execute(User user) {
        Attendance attendance = new Attendance();
        Event event = eventRepository.findFirstByActiveTrue().orElseThrow(()-> new IllegalArgumentException("There is no active event for now"));
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        if (attendanceRepository.existsByUserIdAndEventId(user.getId(), event.getId())) {
            throw new IllegalArgumentException("Attendance already exists");
        }

        LocalTime startWindow = event.getEventCategory().getAttendanceTimeStart().toLocalTime();
        int duration = event.getEventCategory().getAttendanceDuration().intValue();
        LocalTime endWindow = startWindow.plusMinutes(duration);

        if (now.isBefore(startWindow)) {
            throw new IllegalArgumentException("Too early! Attendance for " + event.getEventCategory().getName() + " starts at " + startWindow);
        }

        if (now.isAfter(endWindow)) {
            throw new IllegalArgumentException("Too late! The attendance window closed at " + endWindow);
        }

        attendance.setUser(user);
        attendance.setEvent(event);
        attendanceRepository.save(attendance);
        return ResponseEntity.ok().body("Succesfully joined attendance");
    }
}
