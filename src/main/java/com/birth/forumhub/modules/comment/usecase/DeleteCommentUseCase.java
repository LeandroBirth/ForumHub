package com.birth.forumhub.modules.comment.usecase;

import com.birth.forumhub.modules.comment.entity.CommentEntity;
import com.birth.forumhub.modules.comment.repository.CommentRepository;
import com.birth.forumhub.modules.exception.usecase.ForbiddenException;
import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.topic.entity.TopicEntity;
import com.birth.forumhub.modules.topic.repository.TopicRepository;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class DeleteCommentUseCase {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;


    public DeleteCommentUseCase(CommentRepository commentRepository,
                                UserRepository userRepository,
                                TopicRepository topicRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }


    public void execute(UUID id, UUID getAuthenticatedUserId) {

        CommentEntity commentFound = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found."));

        if (!commentFound.getUser().getId().equals(getAuthenticatedUserId)) {
            throw new ForbiddenException("You are not allowed to delete a comment for another user");
        }

        UserEntity user = userRepository.findById(getAuthenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        TopicEntity topic = topicRepository.findById(commentFound.getTopic().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found."));
        if (!user.participatingInForum(topic.getForum())) {
            throw new ForbiddenException("You are not allowed to update a comment in a topic you do not participate in");
        }

        commentRepository.delete(commentFound);
        topic.decrementComments();
        topicRepository.save(topic);
    }
}