package com.birth.forumhub.modules.forum.usecase;

import com.birth.forumhub.modules.exception.usecase.ResourceAlreadyExistsException;
import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.forum.controller.dto.ForumCreateRequestDTO;
import com.birth.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import com.birth.forumhub.modules.forum.mapper.ForumMapper;
import com.birth.forumhub.modules.forum.repository.ForumRepository;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CreateForumUseCase {

    private final ForumRepository forumRepository;
    private final UserRepository userRepository;


    public CreateForumUseCase(ForumRepository forumRepository,
                              UserRepository userRepository) {
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public ForumResponseDTO execute(ForumCreateRequestDTO requestDTO, UUID authenticatedUserId) {

        UserEntity user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (forumRepository.existsByName(requestDTO.name()).equals(true)) {
            throw new ResourceAlreadyExistsException("Forum already exists.");
        }

        ForumEntity forum = ForumMapper.toEntity(requestDTO, user);
        forum.addParticipant(user);
        user.addOwnedForum(forum);

        userRepository.save(user);
        return ForumMapper.toResponseDTO(forum);
    }
}
