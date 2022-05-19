import {Component, OnInit} from '@angular/core';
import {Post} from 'src/app/models/Post';
import {User} from "../../models/User";
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {CommentService} from "../../service/comment.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  posts: Post[] = [];
  isUserDataLoaded: boolean = false;
  isPostLoaded: boolean = false;
  user: User = {
    id: '',
    email: '',
    login: '',
    firstname: '',
    lastname: '',
    bio: ''
  };

  constructor(private postService: PostService,
              private userService: UserService,
              private commentService: CommentService,
              private notificationService: NotificationService,
              private imageService: ImageUploadService) {
  }

  ngOnInit(): void {
    this.postService.getAllPosts()
      .subscribe(data => {
        console.log(data);
        this.posts = data;
        this.isPostLoaded = true;
      });

    console.log(this.posts);

    this.userService.getCurrentUser()
      .subscribe(data => {
        this.user = data;
        this.isUserDataLoaded = true;
      })
  }

  getImageToPost(posts: Post[]): any {
    posts.forEach(post => {
      this.imageService.getImageToPost(post.id)
        .subscribe(data => {
          post.imageBytes = data.imageBytes;
        });
    });
    return posts;
  }

  getCommentsToPost(posts: Post[]): any {
    posts.forEach(post => {
      this.commentService.getComments(post.id)
        .subscribe(data => {
          post.comments = data;
        })
    });
    return posts;
  }


  likePost(postId: string, postIndex: number): void {
    const post = this.posts[postIndex];
    console.log(post);

    if (post.userLike) {
      this.postService.likePost(postId)
        .subscribe((data) => {
          if (post.userLike) {
            if (data.isLiked) {
              post.userLike.push(this.user.login);
              this.notificationService.showSnackBar("Liked!")
            } else {
              const index = post.userLike.indexOf(this.user.login, 0);
              post.userLike.splice(index, 1);
            }
          }
        }, error => {
          this.notificationService.showSnackBar("error: " + error.message);
        });
    }
  }

  postComment(message: string, postId: string, postIndex: number): void {
    const post = this.posts[postIndex];
    console.log(post);
    console.log(message);
    this.commentService.addComment(postId, message)
      .subscribe(data => {
        console.log(data);
        post.comments?.push(data)
      });
  }

  formatImage(image: any): any {
    if (image == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + image;
  }

}
