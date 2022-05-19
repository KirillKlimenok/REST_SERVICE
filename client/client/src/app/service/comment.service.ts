import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from "rxjs";

const COMMENT_API = 'http://localhost:8080/api/comment/'

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) {
  }

  addComment(postId: string, message: string): Observable<any> {
    return this.http.post(COMMENT_API + postId, {
      comment: message,
    });
  }

  getComments(postId: string): Observable<any> {
    return this.http.get(COMMENT_API + postId + '/all')
  }

  delete(commentId: string): Observable<any> {
    return this.http.delete(COMMENT_API + commentId);
  }
}
