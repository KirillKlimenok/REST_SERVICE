package com.inst.testprogectinst.service;

import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Username not found:" + username));
        return build(user);
    }

    public User loadUserById(UUID userId) {
        return userRepository.findUserById(userId).orElse(null);
    }

    public static User build(User user) {
        user.setAuthorities(List.of(new SimpleGrantedAuthority(user.getRole())));
        return user;
    }
}
