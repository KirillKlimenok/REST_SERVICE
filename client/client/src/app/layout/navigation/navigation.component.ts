import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../../service/token-storage.service";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {NotificationService} from "../../service/notification.service";
import {User} from "../../models/User";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isLoggedIn: boolean = false;
  isDataLoaded: boolean = false;
  user: any;

  constructor(private tokenService: TokenStorageService,
              private userService: UserService,
              private router: Router,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenService.getToken();

    if (this.isLoggedIn) {
      this.userService.getCurrentUser()
        .subscribe((data) => {
          this.user = data;
          this.isDataLoaded = true;
        })
    }
  }

  logout(): void {
    this.tokenService.logOut();
    this.router.navigate(['/login'])
      .then(() => this.notificationService.showSnackBar("Logout"));
  }

}
