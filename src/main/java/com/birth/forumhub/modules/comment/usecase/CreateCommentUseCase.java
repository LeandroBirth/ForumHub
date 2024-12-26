package com.birth.forumhub.modules.comment.usecase;

import com.birth.forumhub.modules.comment.controller.dto.CommentCreateRequestDTO;
import com.birth.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import com.birth.forumhub.modules.comment.entity.CommentEntity;
import com.birth.forumhub.modules.comment.mapper.CommentMapper;
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
public class CreateCommentUseCase {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public CreateCommentUseCase(CommentMapper commentMapper,
                                CommentRepository commentRepository,
                                UserRepository userRepository,
                                TopicRepository topicRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }


    public CommentResponseDTO execute(CommentCreateRequestDTO requestDTO, UUID authenticatedUserId) {

        UserEntity user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        TopicEntity topic = topicRepository.findById(UUID.fromString(requestDTO.topicId()))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found."));

        if (!user.participatingInForum(topic.getForum())) {
            throw new ForbiddenException("You're not allowed to create a comment for this topic.");
        }

        CommentEntity parentComment = null;
        if (requestDTO.parentCommentId() != null) {
            parentComment = commentRepository.findById(UUID.fromString(requestDTO.parentCommentId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found."));

            if (!parentComment.getTopic().equals(topic)) {
                throw new IllegalArgumentException("Parent comment doesn't belong to the specified topic.");
            }
        }

        CommentEntity newComment = commentMapper.toEntity(requestDTO, user, topic, parentComment);
        commentRepository.save(newComment);

        topic.incrementComments();
        topicRepository.save(topic);

        return commentMapper.toResponseDTO(newComment);
    }
}
