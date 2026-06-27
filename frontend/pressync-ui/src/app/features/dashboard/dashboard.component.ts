import { Component, signal } from '@angular/core';

type ScheduleItem = {
  id: number;
  title: string;
  time: string;
  location: string;
  status: 'live' | 'upcoming' | 'completed';
};

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  user = signal({ name: 'Alex', surname: 'Morgan' });

  todaySchedule = signal<ScheduleItem[]>([
    {
      id: 1,
      title: 'CS-101 Advanced Programming',
      time: '09:30 AM',
      location: 'Lab A • Block C',
      status: 'live'
    },
    {
      id: 2,
      title: 'Data Structures Tutorial',
      time: '11:00 AM',
      location: 'Room 204 • Block B',
      status: 'upcoming'
    },
    {
      id: 3,
      title: 'Applied AI Seminar',
      time: '02:00 PM',
      location: 'Auditorium • Main Hall',
      status: 'completed'
    }
  ]);
}
