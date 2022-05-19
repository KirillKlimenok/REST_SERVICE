package com.inst.testprogectinst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    @NotEmpty
    private String login;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    private String bio;
}
