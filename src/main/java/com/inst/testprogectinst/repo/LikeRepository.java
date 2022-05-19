package com.inst.testprogectinst.repo;

import com.inst.testprogectinst.entity.UserLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends CrudRepository<UserLike, UUID> {
    Optional<Integer> countAllByPostId(UUID postId);

    List<UserLike> findAllByPostIdOrderByCreatedAtDesc(UUID postId);

    boolean deleteUserLikeByUserIdAndPostId(UUID userId, UUID postId);

    Optional<UserLike> findLikeByUserIdAndPostId(UUID userId, UUID postId);

    void deleteAllByPostId(UUID postId);
}
