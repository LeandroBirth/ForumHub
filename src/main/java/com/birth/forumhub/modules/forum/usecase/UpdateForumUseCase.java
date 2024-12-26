package com.birth.forumhub.modules.forum.usecase;

import com.birth.forumhub.modules.exception.usecase.ForbiddenException;
import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import com.birth.forumhub.modules.forum.controller.dto.ForumUpdateRequestDTO;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import com.birth.forumhub.modules.forum.mapper.ForumMapper;
import com.birth.forumhub.modules.forum.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class UpdateForumUseCase {

    private final ForumRepository forumRepository;
    private final ForumMapper forumMapper;

    public UpdateForumUseCase(ForumRepository forumRepository,
                              ForumMapper forumMapper) {
        this.forumRepository = forumRepository;
        this.forumMapper = forumMapper;
    }


    public ForumResponseDTO execute(UUID id, ForumUpdateRequestDTO requestDTO, UUID authenticatedUserId) {
        ForumEntity forumFound = forumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forum not found."));

        if (!forumFound.getOwner().getId().equals(authenticatedUserId)) {
            throw new ForbiddenException("You are not allowed to update this forum.");
        }

        if (requestDTO == null
                || (requestDTO.name() == null
                && requestDTO.description() == null)) {

            throw new IllegalArgumentException("""
                    You must provide at least one field to update:
                    - name
                    - description
                    """);
        }

        forumFound.setName(requestDTO.name() != null ? requestDTO.name() : forumFound.getName());
        forumFound.setDescription(requestDTO.description() != null ? requestDTO.description() : forumFound.getDescription());
        forumFound.setUpdatedAt(Instant.now());

        return forumMapper.toResponseDTO(forumRepository.save(forumFound));
    }
}
