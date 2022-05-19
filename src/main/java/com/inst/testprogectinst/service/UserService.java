package com.inst.testprogectinst.service;

import com.inst.testprogectinst.dto.UserDto;
import com.inst.testprogectinst.entity.Role;
import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.exception.UserExistException;
import com.inst.testprogectinst.payload.request.SignUpRequest;
import com.inst.testprogectinst.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User create(SignUpRequest signUpRequest) {
        try {
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .email(signUpRequest.getEmail())
                    .login(signUpRequest.getLogin())
                    .firstname(signUpRequest.getFirstname())
                    .lastname(signUpRequest.getLastName())
                    .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                    .role(Role.USER.name())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UserExistException("any text");
        }
    }

    public User updateUser(UserDto userDto, Principal principal) { //Principal - хранит данные пользователя, id/login
        User user = getUserByPrincipal(principal);
        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getLastname());
        user.setBio(userDto.getBio());


        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User getUserById(UUID userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(()->new UsernameNotFoundException(""));
    }

    private User getUserByPrincipal(Principal principal) {
        String login = principal.getName();
        return userRepository.findUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Login not found"));
    }
}
