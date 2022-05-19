package com.inst.testprogectinst.web;

import com.inst.testprogectinst.dto.CommentDto;
import com.inst.testprogectinst.facade.CommentFacade;
import com.inst.testprogectinst.service.CommentService;
import com.inst.testprogectinst.service.UserService;
import com.inst.testprogectinst.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;


    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable String postId, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        CommentDto resComment = commentFacade.commentToDto(
                commentService.saveComment(UUID.fromString(postId), commentDto, principal)
        );
        resComment.setFirstname(userService.getUserById(UUID.fromString(resComment.getFirstname())).getFirstname());

        return new ResponseEntity<>(resComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentsToPost(@PathVariable String postId) {
        List<CommentDto> commentDtos = commentService.getAllCommentsForPost(UUID.fromString(postId))
                .stream()
                .map(commentFacade::commentToDto)
                .toList();

        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(UUID.fromString(commentId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
