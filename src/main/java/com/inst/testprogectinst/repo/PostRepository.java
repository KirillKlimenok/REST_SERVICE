package com.inst.testprogectinst.repo;

import com.inst.testprogectinst.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends CrudRepository<Post, UUID> {

    List<Post> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> deletePostByIdAndUserId(UUID id, UUID userId);

    Optional<Post> findPostByIdAndUserId(UUID id, UUID userId);
}
