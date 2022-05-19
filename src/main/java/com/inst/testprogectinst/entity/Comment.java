package com.inst.testprogectinst.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private UUID id;
    private UUID postId;
    private UUID userId;
    private String comment;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
