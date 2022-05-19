package com.inst.testprogectinst.service;

import com.inst.testprogectinst.dto.CommentDto;
import com.inst.testprogectinst.entity.Comment;
import com.inst.testprogectinst.entity.Post;
import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.exception.PostNotFoundException;
import com.inst.testprogectinst.repo.CommentRepository;
import com.inst.testprogectinst.repo.PostRepository;
import com.inst.testprogectinst.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public Comment saveComment(UUID postId, CommentDto commentDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findPostByIdAndUserId(postId, user.getId()).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Comment comment = Comment.builder()
                .id(UUID.randomUUID())
                .postId(post.getId())
                .userId(user.getId())
                .comment(commentDto.getComment())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(post.getId());
    }

    public void deleteComment(UUID commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Login not found"));
    }
}
