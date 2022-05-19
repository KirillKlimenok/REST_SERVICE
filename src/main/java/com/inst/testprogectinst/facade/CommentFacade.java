package com.inst.testprogectinst.facade;

import com.inst.testprogectinst.dto.CommentDto;
import com.inst.testprogectinst.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

    public CommentDto commentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .firstname(comment.getUserId().toString())
                .comment(comment.getComment())
                .build();
    }
}
