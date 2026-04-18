export type IsoDate = string;
export type IsoDateTime = string;
export type TimeString = string;

export interface AuthenticationRequest {
  email: string;
  password: string;
}

export interface AuthenticationResponse {
  token: string;
}

export interface UserCreateRequest {
  name: string;
  surname: string;
  email: string;
  password: string;
}

export interface UserGet {
  id: number;
  name: string;
  surname: string;
  email: string;
  role: string;
  active: boolean;
}

export type RepeatableType = 'DAILY' | 'WEEKLY' | 'BIWEEKLY' | 'MONTHLY' | 'YEARLY';
export type RepeatsOnSpecificDay = 'NO' | 'MON' | 'TUE' | 'WED' | 'THU' | 'FRI' | 'SAT' | 'SUN';

export interface EventCategoryConfig {
  id?: number;
  repeatableType?: RepeatableType | null;
  repeatsOnSpecificDay?: RepeatsOnSpecificDay | null;
  baseDate?: IsoDate | null;
}

export interface EventCategory {
  id: number;
  name: string;
  startingTime: TimeString;
  endTime: TimeString;
  attendanceTimeStart: TimeString;
  attendanceDuration: number;
  repeatable: boolean;
  specificDate?: IsoDate | null;
  categoryConfig?: EventCategoryConfig | null;
}

export interface CreateEventCategoryRequest {
  name: string;
  startingTime: TimeString;
  endTime: TimeString;
  attendanceTimeStart: TimeString;
  attendanceDuration: number;
  repeatable?: boolean | null;
  configId?: number | null;
  repeatableType?: RepeatableType | null;
  repeatsOnSpecificDay?: RepeatsOnSpecificDay | null;
  baseDate?: IsoDate | null;
}

export interface UpdateEventCategoryRequest {
  name: string;
  startingTime: TimeString;
  endTime: TimeString;
  attendanceTimeStart: TimeString;
  attendanceDuration: number;
  repeatable?: boolean | null;
  configId?: number | null;
  repeatableType?: RepeatableType | null;
  repeatsOnSpecificDay?: RepeatsOnSpecificDay | null;
  baseDate?: IsoDate | null;
}

export interface Event {
  id?: number;
  eventCategory: EventCategory;
  active: boolean;
  archived: boolean;
  date: IsoDate;
}

export interface EventCategoryStats {
  averageAttendance: number;
  maxAttendance: number;
  minAttendance: number;
  projectedNextAttendance: number;
}

export interface AttendanceCreateRequest {
  userId: number;
  eventId: number;
}

export interface AttendanceGet {
  id: number;
  user: UserGet;
  event: Event;
  date: IsoDateTime;
}

export interface AttendanceDetail {
  id: number;
  user: UserGet;
  event: Event;
  joinedAt: IsoDateTime;
}
