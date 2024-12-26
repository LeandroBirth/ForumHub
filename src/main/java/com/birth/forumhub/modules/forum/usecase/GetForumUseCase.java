package com.birth.forumhub.modules.forum.usecase;

import com.birth.forumhub.modules.exception.usecase.ResourceNotFoundException;
import com.birth.forumhub.modules.forum.controller.dto.ForumResponseDTO;
import com.birth.forumhub.modules.forum.entity.ForumEntity;
import com.birth.forumhub.modules.forum.mapper.ForumMapper;
import com.birth.forumhub.modules.forum.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class GetForumUseCase {

    private final ForumRepository forumRepository;
    private final ForumMapper forumMapper;

    public GetForumUseCase(ForumRepository forumRepository,
                           ForumMapper forumMapper) {
        this.forumRepository = forumRepository;
        this.forumMapper = forumMapper;
    }


    public ForumResponseDTO execute(UUID id) {

        ForumEntity forumFound = forumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forum not found."));

        return forumMapper.toResponseDTO(forumFound);
    }
}
