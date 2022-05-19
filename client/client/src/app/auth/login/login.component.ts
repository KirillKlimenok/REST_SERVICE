import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from 'src/app/service/auth.service';
import {TokenStorageService} from "../../service/token-storage.service";
import {NotificationService} from "../../service/notification.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup = this.createLoginForm();

  constructor(
    private authService: AuthService,
    private tokenStore: TokenStorageService,
    private notificationService: NotificationService,
    private router: Router,
    private fb: FormBuilder,
  ) {
    if (this.tokenStore.getUser()) {
      this.router.navigate(['main']).then(() => this.notificationService.showSnackBar('Successfully login'));
    }
  }

  ngOnInit(): void {
    this.loginForm = this.createLoginForm();
  }

  createLoginForm(): FormGroup {
    return this.fb.group({
      login: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])]
    });
  }

  submit(): void {
    this.authService.login({
      login: this.loginForm.value.login,
      password: this.loginForm.value.password
    }).subscribe(data => {
      console.log(data);

      this.tokenStore.saveToken(data.token);
      this.tokenStore.saveUser(data);


      this.router.navigate(['main']).then(() => this.notificationService.showSnackBar('Successfully login'));
    }, error => {
      console.log(error);
      this.notificationService.showSnackBar(error.message);
    });
  }

}
