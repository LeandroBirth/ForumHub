package com.birth.forumhub.modules.topic.usecase;

import com.birth.forumhub.modules.exception.usecase.ForbiddenException;
import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import com.birth.forumhub.modules.forum.repository.ForumRepository;
import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.topic.repository.TopicRepository;
import com.birth.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class DeleteTopicUseCase {

    private final TopicRepository topicRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    public DeleteTopicUseCase(TopicRepository topicRepository,
                              ForumRepository forumRepository,
                              UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
    }


    public void execute(UUID id, UUID authenticatedUserId) {
        TopicEntity topicDB = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found."));

        if (!topicDB.getCreator().getId().equals(authenticatedUserId)) {
            throw new ForbiddenException("You are not allowed to delete this topic.");
        }

        ForumEntity forumFound = forumRepository.findById(topicDB.getForum().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Forum not found."));
        forumFound.decrementTopicsCount();
        forumFound.removeTopic(topicDB);

        topicRepository.delete(topicDB);
        forumRepository.save(forumFound);
    }
}
