package com.inst.testprogectinst.facade;

import com.inst.testprogectinst.dto.UserDto;
import com.inst.testprogectinst.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId().toString())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .login(user.getLogin())
                .bio(user.getBio())
                .build();
    }
}
