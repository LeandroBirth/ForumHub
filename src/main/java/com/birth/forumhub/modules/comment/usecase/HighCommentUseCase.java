package com.birth.forumhub.modules.comment.usecase;
import com.birth.forumhub.modules.comment.entity.CommentEntity;
import com.birth.forumhub.modules.comment.entity.CommentHighsEntity;
import com.birth.forumhub.modules.comment.repository.CommentHighsRepository;
import com.birth.forumhub.modules.comment.repository.CommentRepository;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HighCommentUseCase {

    private final CommentHighsRepository commentHighsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public HighCommentUseCase(CommentHighsRepository commentHighsRepository,
                              CommentRepository commentRepository,
                              UserRepository userRepository) {
        this.commentHighsRepository = commentHighsRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public void execute(UUID commentId, UUID authenticatedUserId) {
        commentHighsRepository.findByComment_IdAndUser_Id(commentId, authenticatedUserId)
                .ifPresent(commentHighsEntity -> {
                    throw new IllegalArgumentException("Comment already highed");
                });

        CommentEntity commentFound = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("Comment not found")
        );

        UserEntity authenticatedUserEntity = userRepository.findById(authenticatedUserId).orElseThrow(
                () -> new IllegalArgumentException("Comment not found")
        );

        commentFound.incrementHighs();
        commentHighsRepository.save(new CommentHighsEntity(commentFound, authenticatedUserEntity));
    }
}
