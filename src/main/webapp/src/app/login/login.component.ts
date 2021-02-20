import { Component, NgZone, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';

declare let gapi: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  progress: boolean = false;

  constructor(
      private userService: UserService,
      private messageService: MessageService,
      private router: Router,
      private ngZone: NgZone
  ) { }

  ngOnInit(): void {
    this.renderButton();
  }

  onSuccess(googleUser) {
    const idToken = googleUser.getAuthResponse().id_token;
    this.progress = true;
    this.userService.login({idToken})
        .pipe(
            finalize(() => this.progress = false)
        ).subscribe(res => {
      this.ngZone.run(() => {
        this.router.navigate(['/list']);
      });
    }, (err: HttpErrorResponse) => {
      this.ngZone.run(() => {
        this.showError(err.message || JSON.stringify(err.error));
        this.progress = false;
      })
    });
  }

  onFailure(error) {
    this.showError(error);
  }

  showError(err) {
    console.error(err);
    this.messageService.add({severity:'error', summary:'Ошибка авторизации', detail:err});
  }

  renderButton() {
    gapi.signin2.render('singin-button', {
      'scope': 'profile email',
      'width': 240,
      'height': 50,
      'longtitle': true,
      'theme': 'dark',
      'onsuccess': (user) => this.onSuccess(user),
      'onfailure': (error) => this.onFailure(error)
    });
  }

}
