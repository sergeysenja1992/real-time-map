import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

declare let gapi: any;

interface LoginDto {
  idToken: string;
}

interface User {
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  login(body: LoginDto): Observable<any> {
    return this.httpClient.post('/api/user/login', body);
  }

  getCurrentAccount(): Observable<any> {
    return this.httpClient.get('/api/user/account');
  }

  logout() {

    gapi.auth2.getAuthInstance().disconnect();
  }
}
