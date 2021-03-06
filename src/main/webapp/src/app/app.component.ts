import { Component, OnInit } from '@angular/core';
import { MessageService, PrimeNGConfig } from 'primeng/api';
import { UserService } from './services/user.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [MessageService]
})
export class AppComponent implements OnInit {

  constructor(private primengConfig: PrimeNGConfig,
              userService: UserService,
              activeRoute: ActivatedRoute,
              router: Router) {
    if (window.location.hash === '#/login') {
      return;
    }

    userService.getCurrentAccount().subscribe(res => {
      if (!res || !res.key) {
        router.navigate(['/login'])
      }
    }, err => {
      router.navigate(['/login'])
    });
  }

  ngOnInit() {
    this.primengConfig.ripple = true;
  }

}
