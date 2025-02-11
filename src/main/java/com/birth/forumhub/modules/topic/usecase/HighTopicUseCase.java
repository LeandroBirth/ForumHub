package com.birth.forumhub.modules.topic.usecase;

import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.topic.entity.TopicHighsEntity;
import com.birth.forumhub.modules.topic.repository.TopicHighsRepository;
import com.birth.forumhub.modules.topic.repository.TopicRepository;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class HighTopicUseCase {

    private final TopicHighsRepository topicHighsRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;


    public HighTopicUseCase(TopicHighsRepository topicHighsRepository,
                            TopicRepository topicRepository,
                            UserRepository userRepository) {
        this.topicHighsRepository = topicHighsRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }


    public void execute(UUID topicId, UUID authenticatedUserId) {
        topicHighsRepository.findByTopic_IdAndUser_Id(topicId, authenticatedUserId)
                .ifPresent(userHighsEntity -> {
                    throw new IllegalArgumentException("Topic already highed");
                });

        TopicEntity topicFound = topicRepository.findById(topicId).orElseThrow(
                () -> new IllegalArgumentException("Topic not found")
        );

        UserEntity authenticatedUserEntity = userRepository.findById(authenticatedUserId).orElseThrow(
                () -> new IllegalArgumentException("Topic not found")
        );

        topicFound.incrementHighs();
        topicHighsRepository.save(new TopicHighsEntity(topicFound, authenticatedUserEntity));
    }
}
