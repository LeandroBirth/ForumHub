package com.birth.forumhub.modules.forum.usecase;

import com.birth.forumhub.modules.exception.usecase.ForbiddenException;
import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import com.birth.forumhub.modules.forum.repository.ForumRepository;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class DeleteForumUseCase {

    private final ForumRepository forumRepository;
    private final UserRepository userRepository;


    public DeleteForumUseCase(ForumRepository forumRepository,
                              UserRepository userRepository) {
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
    }

    public void execute(UUID id, UUID authenticatedUserId) {
        ForumEntity forumDB = forumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forum not found."));

        if (!forumDB.getOwner().getId().equals(authenticatedUserId)) {
            throw new ForbiddenException("You are not allowed to delete this forum.");
        }

        UserEntity owner = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        owner.removeOwnedForum(forumDB);
        owner.removeParticipatingForum(forumDB);
        forumDB.removeOwner();
        forumDB.removeParticipant(owner);
        forumRepository.delete(forumDB);
    }
}
