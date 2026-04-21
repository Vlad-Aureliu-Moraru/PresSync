import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserGetAllDTO {
  id: number;
  name: string;
  surname: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);

  getAllUsers(): Observable<UserGetAllDTO[]> {
    return this.http.get<UserGetAllDTO[]>(`${environment.apiUrl}/user`);
  }

  getUserById(id: number): Observable<UserGetAllDTO> {
    return this.http.get<UserGetAllDTO>(`${environment.apiUrl}/user/${id}`);
  }
}
