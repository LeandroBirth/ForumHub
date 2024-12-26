package com.birth.forumhub.modules.user.controller.dto;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import java.time.Instant;
import java.util.UUID;



public record ParticipatesInDTO(UUID id,
                                String name,
                                Instant createdAt) {
    public static ParticipatesInDTO from(ForumEntity forum) {
        return new ParticipatesInDTO(
                forum.getId(),
                forum.getName(),
                forum.getCreatedAt()
        );
    }
}