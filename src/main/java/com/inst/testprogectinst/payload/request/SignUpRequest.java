package com.inst.testprogectinst.payload.request;

import com.inst.testprogectinst.annotation.PasswordMatches;
import com.inst.testprogectinst.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@PasswordMatches
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @Email(message = "It should have email format ")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your firstname")
    private String firstname;

    @NotEmpty(message = "Please enter your lastName")
    private String lastName;

    @NotEmpty(message = "Please enter your login")
    private String login;

    @NotEmpty(message = "Please enter your password")
    private String password;

    @NotEmpty(message = "Please enter your confirmPassword")
    private String confirmPassword;

}
