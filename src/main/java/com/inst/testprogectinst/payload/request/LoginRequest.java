package com.inst.testprogectinst.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "login cannot be empty")
    private String login;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
