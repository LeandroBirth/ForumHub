package com.birth.forumhub.modules.topic.mapper;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import com.birth.forumhub.modules.topic.controller.dto.TopicCreateRequestDTO;
import com.birth.forumhub.modules.topic.controller.dto.TopicDetailsResponseDTO;
import com.birth.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import com.birth.forumhub.modules.topic.controller.dto.TopicUpdateRequestDTO;
import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.user.entity.UserEntity;

import java.util.List;


public class TopicMapper {


    public static TopicEntity toEntity(TopicCreateRequestDTO dto, ForumEntity forum, UserEntity creator) {
        return new TopicEntity(
                dto.title(),
                dto.content(),
                creator,
                forum
        );
    }


    public static TopicEntity toEntity(TopicUpdateRequestDTO dto) {
        return new TopicEntity(
                dto.title(),
                dto.content()
        );
    }

    public static TopicResponseDTO toResponseDTO(TopicEntity topic) {
        return new TopicResponseDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getForum().getId(),
                topic.getCreator().getId(),
                topic.getCreator().getUsername(),
                topic.getHighsCount(),
                topic.getCommentsCount(),
                topic.getCreatedAt(),
                topic.getUpdatedAt()
        );
    }


    public static TopicDetailsResponseDTO toDetailsResponseDTO(TopicEntity topic) {
        return new TopicDetailsResponseDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getForum().getId(),
                topic.getCreator().getId(),
                topic.getCreator().getUsername(),
                topic.getHighsCount(),
                topic.getCommentsCount(),
                List.of(), //TODO - Convert TopicMapper to Spring Component and inject CommentMapper
//                topic.getComments().stream().map(CommentMapper::toResponseDTO).toList(),
                topic.getCreatedAt(),
                topic.getUpdatedAt()
        );
    }
}
