import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {TokenStorageService} from "../../service/token-storage.service";
import {PostService} from "../../service/post.service";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";
import {UserService} from "../../service/user.service";
import {EditUserComponent} from "../edit-user/edit-user.component";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  isUserDataLoaded: boolean = false;
  user: User = {
    id: '',
    email: '',
    login: '',
    firstname: '',
    lastname: '',
    bio: ''
  };
  selectedFile?: File;
  userProfileImage?: File;
  previewImgUrl: any;


  constructor(private tokenService: TokenStorageService,
              private postService: PostService,
              private dialog: MatDialog,
              private notificationService: NotificationService,
              private imageService: ImageUploadService,
              private userService: UserService) {
    this.userService.getCurrentUser()
      .subscribe(data => {
        this.user = data;
        this.isUserDataLoaded = true;
      });

    this.imageService.getProfileImage()
      .subscribe(data => {
        this.userProfileImage = data.imageBytes;
      });
  }

  ngOnInit(): void {
    this.userService.getCurrentUser()
      .subscribe(data => {
        this.user = data;
        this.isUserDataLoaded = true;
      });

    this.imageService.getProfileImage()
      .subscribe(data => {
        this.userProfileImage = data.imageBytes;
      });

  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    const reader = new FileReader();
    if (this.selectedFile)
      reader.readAsDataURL(this.selectedFile);

    reader.onload = () => {
      this.previewImgUrl = reader.result;
    }
  }

  openEditDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "400px";
    dialogConfig.data = {
      user: this.user
    }
    this.dialog.open(EditUserComponent, dialogConfig);
  }

  formatImage(image: any): any {
    if (image == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + image;
  }

  onUpload() {
    if (this.selectedFile != null) {
      this.imageService.uploadImageToUser(this.selectedFile)
        .subscribe(() => {
          this.notificationService.showSnackBar("Image upload successful!");
        });
    }
  }
}
