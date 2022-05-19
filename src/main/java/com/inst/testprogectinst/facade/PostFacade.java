package com.inst.testprogectinst.facade;

import com.inst.testprogectinst.dto.PostDto;
import com.inst.testprogectinst.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDto postToDto(Post post){
        return  PostDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .caption(post.getCaption())
                .location(post.getLocation())
                .title(post.getTitle())
                .build();
    }
}
