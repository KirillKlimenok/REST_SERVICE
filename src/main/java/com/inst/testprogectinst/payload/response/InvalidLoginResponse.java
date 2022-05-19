package com.inst.testprogectinst.payload.response;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class InvalidLoginResponse {
    private final String login;
    private final String password;

    public InvalidLoginResponse() {
        this.login = "Invalid login";
        this.password = "Invalid Password";
    }

}
