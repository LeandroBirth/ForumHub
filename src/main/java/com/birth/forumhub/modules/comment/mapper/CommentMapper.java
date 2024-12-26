package com.birth.forumhub.modules.comment.mapper;

import com.birth.forumhub.modules.comment.controller.dto.CommentCreateRequestDTO;
import com.birth.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import com.birth.forumhub.modules.comment.entity.CommentEntity;
import com.birth.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.topic.mapper.TopicMapper;
import com.birth.forumhub.modules.user.controller.dto.UserResponseDTO;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
public class CommentMapper {

    private CommentMapper() {}
    public CommentEntity toEntity(CommentCreateRequestDTO dto,
                                         UserEntity userEntity,
                                         TopicEntity topicEntity) {
        return new CommentEntity(dto.content(), userEntity, topicEntity);
    }


    public CommentEntity toEntity(CommentCreateRequestDTO dto,
                                         UserEntity userEntity,
                                         TopicEntity topicEntity,
                                         CommentEntity parentComment) {
        if (parentComment == null) {
            return toEntity(dto, userEntity, topicEntity);
        }
        return new CommentEntity(dto.content(), userEntity, topicEntity, parentComment);
    }

    public CommentResponseDTO toResponseDTO(CommentEntity commentEntity) {

        UserResponseDTO user = UserMapper.toResponseDTO(commentEntity.getUser());
        TopicResponseDTO topic = TopicMapper.toResponseDTO(commentEntity.getTopic());
        UUID parentComment = commentEntity.getParentComment() != null ? commentEntity.getParentComment().getId() : null;

        List<CommentResponseDTO> replies = commentEntity.getReplies().stream()
                .map(this::toResponseDTO)
                .toList();

        return new CommentResponseDTO(
                commentEntity.getId(),
                commentEntity.getContent(),
                user.id(),
                topic.id(),
                commentEntity.getHighsCount(),
                parentComment,
                replies,
                commentEntity.getCreatedAt(),
                commentEntity.getUpdatedAt());
    }
}