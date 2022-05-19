package com.inst.testprogectinst.repo;

import com.inst.testprogectinst.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends CrudRepository<Comment, UUID> {
    List<Comment> findAllByPostIdOrderByCreatedAtDesc(UUID postId);
}
