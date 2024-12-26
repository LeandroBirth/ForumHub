package com.birth.forumhub.modules.topic.usecase;

import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.topic.controller.dto.TopicDetailsResponseDTO;
import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.topic.mapper.TopicMapper;
import com.birth.forumhub.modules.topic.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class GetTopicDetailsUseCase {

    private final TopicRepository topicRepository;


    public GetTopicDetailsUseCase(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }


    public TopicDetailsResponseDTO execute(UUID id) {

        TopicEntity topicFound = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found."));
        topicFound.getHighsCount();
        topicFound.getComments();
        topicFound.getCreator();

        return TopicMapper.toDetailsResponseDTO(topicFound);
    }
}
