package com.inst.testprogectinst.payload.response;

import com.inst.testprogectinst.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private UUID id;
    private String title;
    private String captions;
    private String location;
    private Integer like;
    private List<String> userLike;
    private String login;
    private List<CommentDto> comments;
    private byte[] imageBytes;
}
