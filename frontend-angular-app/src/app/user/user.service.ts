import { User, UsersResponse } from './user.component';
import { environment } from './../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getUsers(page: number, size: number): Observable<any> {
    return this.http.get(`${this.url}/api/v1/users?page=${page}&size=${size}`);
  }

  delete(id: string) {
    return this.http.delete(`${this.url}/api/v1/users/${id}`);
  }

  deleteUsers(ids: string) {
    return this.http.delete(`${this.url}/api/v1/users?ids=${ids}`);
  }

  registerUser(user: User) {
    return this.http.post(`${this.url}/api/v1/users`, user);
  }

  editUser(user: User) {
    return this.http.put(`${this.url}/api/v1/users/${user.id}`, user);
  }

  getWelcomeEmail(email:string){
    return this.http.get(`http://localhost:1080/api/emails?to=${email}`);

  }
}
