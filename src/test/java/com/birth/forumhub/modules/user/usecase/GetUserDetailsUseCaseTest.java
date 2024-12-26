package com.birth.forumhub.modules.user.usecase;

import com.birth.forumhub.modules.user.controller.dto.UserResponseDTO;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.mapper.UserMapper;
import com.birth.forumhub.modules.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class GetUserDetailsUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserDetailsUseCase getUserDetailsUseCase;

    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
    }

    @Test
    void testExecute_ShouldReturnUserDetailsSuccessfully() {
        UserEntity userEntity = new UserEntity(
                "John Doe",
                "johndoe",
                "johndoe@mail.com",
                "password");
        UserResponseDTO responseDTO = UserMapper.toResponseDTO(userEntity);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userEntity));

        UserResponseDTO result = getUserDetailsUseCase.execute(userId);

        verify(userRepository).findById(userId);
        assertEquals(responseDTO, result);
    }

    @Test
    void testExecute_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        try {
            getUserDetailsUseCase.execute(userId);
        } catch (Exception e) {
            assertEquals("User with ID " + userId + " not found.", e.getMessage());
        }

        verify(userRepository).findById(userId);
    }
}