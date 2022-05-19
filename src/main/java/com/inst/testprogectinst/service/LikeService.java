package com.inst.testprogectinst.service;

import com.inst.testprogectinst.entity.UserLike;
import com.inst.testprogectinst.entity.User;
import com.inst.testprogectinst.repo.LikeRepository;
import com.inst.testprogectinst.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public List<String> getLikedUserLogin(UUID postId) {
        List<UUID> allByPostIdOrderByCreatedAtDesc = likeRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(UserLike::getUserId)
                .toList();

        List<String> userLoginWhoLike = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (allByPostIdOrderByCreatedAtDesc.contains(user.getId())) {
                userLoginWhoLike.add(user.getLogin());
            }
        }

        return userLoginWhoLike;
    }
}
