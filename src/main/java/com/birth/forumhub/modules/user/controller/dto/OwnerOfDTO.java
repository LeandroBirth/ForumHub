package com.birth.forumhub.modules.user.controller.dto;

import com.birth.forumhub.modules.forum.entity.ForumEntity;

import java.time.Instant;
import java.util.UUID;


public record OwnerOfDTO(UUID id,
                         String name,
                         Instant createdAt) {
    public static OwnerOfDTO from(ForumEntity forum) {
        return new OwnerOfDTO(
                forum.getOwner().getId(),
                forum.getOwner().getName(),
                forum.getOwner().getCreatedAt()
        );
    }
}