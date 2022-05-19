package com.inst.testprogectinst.service;

import com.inst.testprogectinst.dto.PostDto;
import com.inst.testprogectinst.entity.UserLike;
import com.inst.testprogectinst.entity.Post;
import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.exception.PostNotFoundException;
import com.inst.testprogectinst.payload.response.LikeResponse;
import com.inst.testprogectinst.repo.ImageRepository;
import com.inst.testprogectinst.repo.LikeRepository;
import com.inst.testprogectinst.repo.PostRepository;
import com.inst.testprogectinst.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;


    public Post createPost(PostDto postDto, Principal principal) {
        User user = getUserByPrincipal(principal);

        Post post = Post.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .location(postDto.getLocation())
                .caption(postDto.getCaption())
                .title(postDto.getTitle())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPostById(UUID postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUserId(postId, user.getId())
                .orElseThrow(() -> new PostNotFoundException("Post cannot bo found"));
    }

    public List<Post> getAllPostsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public LikeResponse likePost(UUID postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        boolean isLiked = false;
        Post post = postRepository.findPostByIdAndUserId(postId, user.getId())
                .orElseThrow(() -> new PostNotFoundException("Post cannot bo found"));

        Optional<UserLike> likeByUserId = likeRepository.findLikeByUserIdAndPostId(user.getId(), postId);
        System.out.println(likeByUserId);

        if (likeRepository.findLikeByUserIdAndPostId(user.getId(), postId).orElse(null) == null) {
            likeRepository.save(UserLike.builder()
                    .id(UUID.randomUUID())
                    .postId(post.getId())
                    .userId(user.getId())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build());
            isLiked = true;
        } else {
            likeRepository.deleteUserLikeByUserIdAndPostId(user.getId(), postId);
        }
        return new LikeResponse(isLiked);
    }

    public void deletePost(UUID postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = getPostById(postId, principal);
        postRepository.deletePostByIdAndUserId(post.getId(), user.getId());
        likeRepository.deleteAllByPostId(post.getId());
        imageRepository.deleteAllByPostId(post.getId());
    }

    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Login not found"));
    }
}
