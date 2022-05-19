import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private sanckbar: MatSnackBar) {
  }

  public showSnackBar(message: string): void {
    // @ts-ignore
    this.sanckbar.open(message, null, {
      duration: 10000
    });
  }
}
