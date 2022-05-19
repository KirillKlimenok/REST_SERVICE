package com.inst.testprogectinst.repo;

import com.inst.testprogectinst.entity.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends CrudRepository<Image, UUID> {
    Optional<Image> findImageByUserId(UUID userId);

    Optional<Image> findImageByPostId(UUID postId);

    List<Image> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    void deleteAllByPostId(UUID postId);
}
