package com.inst.testprogectinst.web;

import com.inst.testprogectinst.dto.CommentDto;
import com.inst.testprogectinst.dto.PostDto;
import com.inst.testprogectinst.entity.Image;
import com.inst.testprogectinst.entity.Post;
import com.inst.testprogectinst.facade.CommentFacade;
import com.inst.testprogectinst.facade.PostFacade;
import com.inst.testprogectinst.payload.response.LikeResponse;
import com.inst.testprogectinst.payload.response.PostResponse;
import com.inst.testprogectinst.repo.ImageRepository;
import com.inst.testprogectinst.service.CommentService;
import com.inst.testprogectinst.service.LikeService;
import com.inst.testprogectinst.service.PostService;
import com.inst.testprogectinst.service.UserService;
import com.inst.testprogectinst.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/post")
public class PostController {
    private final PostFacade postFacade;
    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;
    private final ImageRepository imageRepository;
    private final CommentService commentService;
    private final CommentFacade commentFacade;

    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Post post = postService.createPost(postDto, principal);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    //todo Переделать
    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> postResponses = postService.getAllPosts()
                .stream()
                .map(postFacade::postToDto)
                .map(getPostDtoPostResponseFunction())
                .collect(Collectors.toList());

        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    @GetMapping("/user/post")
    public ResponseEntity<List<PostResponse>> getAllPostsForUser(Principal principal) {
        List<PostResponse> postDtoList = postService.getAllPostsForUser(principal)
                .stream()
                .map(postFacade::postToDto)
                .map(getPostDtoPostResponseFunction())
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    //todo переделать, чтобы после лайка возвращало значение "isLiked":true/false.
    //todo true значит лайк добавлен. false значит дизлайк
    @PostMapping("{postId}/like")
    public ResponseEntity<LikeResponse> likePost(@PathVariable String postId, Principal principal) {
        LikeResponse likeResponse = postService.likePost(UUID.fromString(postId), principal);

        return new ResponseEntity<>(likeResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId, Principal principal) {
        postService.deletePost(UUID.fromString(postId), principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Function<PostDto, PostResponse> getPostDtoPostResponseFunction() {
        return postDto -> {
            List<String> likedUserLogin = likeService.getLikedUserLogin(postDto.getId());
            byte[] imageBytes = imageRepository.findImageByPostId(postDto.getId()).orElse(new Image()).getImageBytes();
            List<CommentDto> commentDtos = commentService.getAllCommentsForPost(postDto.getId()).stream()
                    .map(comment -> {
                        CommentDto commentDto = commentFacade.commentToDto(comment);
                        commentDto.setFirstname(userService.getUserById(UUID.fromString(commentDto.getFirstname())).getFirstname());
                        return commentDto;
                    })
                    .toList();
            return PostResponse.builder()
                    .id(postDto.getId())
                    .title(postDto.getTitle())
                    .captions(postDto.getCaption())
                    .location(postDto.getLocation())
                    .like(likedUserLogin.size())
                    .userLike(likedUserLogin)
                    .login(userService.getUserById(postDto.getUserId()).getLogin())
                    .imageBytes(imageBytes)
                    .comments(commentDtos)
                    .build();
        };
    }
}
