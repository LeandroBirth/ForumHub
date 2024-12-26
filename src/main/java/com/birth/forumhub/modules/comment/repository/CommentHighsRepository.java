package com.birth.forumhub.modules.comment.repository;

import com.birth.forumhub.modules.comment.entity.CommentHighsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface CommentHighsRepository extends JpaRepository<CommentHighsEntity, String> {
    Optional<CommentHighsEntity> findByComment_IdAndUser_Id(UUID commentId, UUID userId);
}
