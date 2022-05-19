package com.inst.testprogectinst.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_likes")
@Entity(name = "user_likes")
public class UserLike {
    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "post_id")
    private UUID postId;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLike userLike = (UserLike) o;
        return Objects.equals(id, userLike.id) && Objects.equals(userId, userLike.userId) && Objects.equals(postId, userLike.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, postId);
    }
}
