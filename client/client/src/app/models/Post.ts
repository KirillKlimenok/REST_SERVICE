import {Comment} from "./Comment";

export interface Post{
  id:string;
  title:string;
  captions:string;
  location:string;
  imageBytes?:File;
  like:number;
  userLike:string[];
  comments?:Comment[];
  login?:string;
}
