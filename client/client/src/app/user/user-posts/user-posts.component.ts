import { Component, OnInit } from '@angular/core';
import {CommentService} from "../../service/comment.service";
import {Post} from "../../models/Post";
import {PostService} from "../../service/post.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";

@Component({
  selector: 'app-user-posts',
  templateUrl: './user-posts.component.html',
  styleUrls: ['./user-posts.component.css']
})
export class UserPostsComponent implements OnInit {

  isUserPostsLoaded = false;
  // @ts-ignore
  posts: Post [];

  constructor(private postService: PostService,
              private imageService: ImageUploadService,
              private commentService: CommentService,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.postService.getPostForCurrentUser()
      .subscribe(data => {
        console.log(data);
        this.posts = data;
        this.isUserPostsLoaded = true;
      });
  }

  removePost(post: Post, index: number): void {
    console.log(post);
    const result = confirm('Do you really want to delete this post?');
    if (result) {
      this.postService.delete(post.id)
        .subscribe(() => {
          this.posts.splice(index, 1);
          this.notificationService.showSnackBar('Post deleted');
        });
    }
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  deleteComment(commentId: string, postIndex: number, commentIndex: number): void {
    const post = this.posts[postIndex];

    this.commentService.delete(commentId)
      .subscribe(() => {
        this.notificationService.showSnackBar('Comment removed');
        if(post.comments)
        post.comments.splice(commentIndex, 1);
      });
  }

}
