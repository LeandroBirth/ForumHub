package com.birth.forumhub.modules.topic.usecase;

import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import com.birth.forumhub.modules.forum.repository.ForumRepository;
import com.birth.forumhub.modules.topic.controller.dto.TopicCreateRequestDTO;
import com.birth.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.topic.mapper.TopicMapper;
import com.birth.forumhub.modules.topic.repository.TopicRepository;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CreateTopicUseCase {

    private final TopicRepository topicRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    public CreateTopicUseCase(TopicRepository topicRepository,
                              ForumRepository forumRepository,
                              UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
    }


    public TopicResponseDTO execute(TopicCreateRequestDTO requestDTO, UUID authenticatedUserId) {

        ForumEntity forum = forumRepository.findById(UUID.fromString(requestDTO.forumId()))
                .orElseThrow(() -> new ResourceNotFoundException("Forum not found."));
        UserEntity user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        TopicEntity topic = TopicMapper.toEntity(requestDTO, forum, user);
        topic.setCreator(user);
        topic.setForum(forum);

        TopicEntity topicSaved = topicRepository.save(topic);

        forum.incrementTopicsCount();
        forumRepository.save(forum);

        return TopicMapper.toResponseDTO(topicSaved);
    }
}
