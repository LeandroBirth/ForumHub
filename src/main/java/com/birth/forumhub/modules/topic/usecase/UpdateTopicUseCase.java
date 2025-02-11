package com.birth.forumhub.modules.topic.usecase;

import com.birth.forumhub.modules.exception.usecase.ForbiddenException;
import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import com.birth.forumhub.modules.topic.controller.dto.TopicUpdateRequestDTO;
import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.topic.mapper.TopicMapper;
import com.birth.forumhub.modules.topic.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class UpdateTopicUseCase {

    private final TopicRepository topicRepository;


    public UpdateTopicUseCase(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }


    public TopicResponseDTO execute(UUID id, TopicUpdateRequestDTO requestDTO, UUID autheticatedUserId) {

        TopicEntity topicFound = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found."));

        if (!topicFound.getCreator().getId().equals(autheticatedUserId)) {
            throw new ForbiddenException("You are not allowed to update this topic.");
        }

        if (requestDTO == null
                || (requestDTO.title() == null
                && requestDTO.content() == null)) {

            throw new IllegalArgumentException("""
                    You must provide at least one field to update:
                    - title
                    - content
                    """);
        }

        topicFound.setTitle(requestDTO.title() != null ? requestDTO.title() : topicFound.getTitle());
        topicFound.setContent(requestDTO.content() != null ? requestDTO.content() : topicFound.getContent());
        topicFound.setUpdatedAt(Instant.now());

        return TopicMapper.toResponseDTO(topicRepository.save(topicFound));
    }
}
