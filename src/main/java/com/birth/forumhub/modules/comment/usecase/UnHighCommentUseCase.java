package com.birth.forumhub.modules.comment.usecase;

import com.birth.forumhub.modules.comment.repository.CommentHighsRepository;
import com.birth.forumhub.modules.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UnHighCommentUseCase {

    private final CommentHighsRepository commentHighsRepository;
    private final CommentRepository commentRepository;


    public UnHighCommentUseCase(CommentHighsRepository commentHighsRepository,
                                CommentRepository commentRepository) {
        this.commentHighsRepository = commentHighsRepository;
        this.commentRepository = commentRepository;
    }

    public void execute(UUID commentId, UUID authenticatedUserId) {
        commentHighsRepository.findByComment_IdAndUser_Id(commentId, authenticatedUserId)
                .ifPresentOrElse(
                        commentHighsRepository::delete,
                    () -> {
                        throw new IllegalArgumentException("Comment not highed");
                    }
                );

        commentRepository.findById(commentId).ifPresent((comment) -> {
            comment.decrementHighs();
            commentRepository.save(comment);
        });
    }
}
