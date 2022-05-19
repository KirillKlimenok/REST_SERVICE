package com.inst.testprogectinst.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenSuccessResponse {

    private boolean success;
    private String token;
}
