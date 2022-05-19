package com.inst.testprogectinst.web;

import com.inst.testprogectinst.dto.UserDto;
import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.facade.UserFacade;
import com.inst.testprogectinst.service.UserService;
import com.inst.testprogectinst.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable String userId) {
        User user = userService.getUserById(UUID.fromString(userId));
        UserDto userDto = userFacade.userToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDto, principal);
        UserDto updateUser = userFacade.userToUserDto(user);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

}
