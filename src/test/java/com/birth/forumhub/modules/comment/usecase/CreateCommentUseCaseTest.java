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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class CreateCommentUseCaseTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private CreateCommentUseCase createCommentUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ShouldCreateComment_WhenDataIsValid() {
        UUID userId = UUID.randomUUID();
        UUID topicId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        UserEntity user = mock(UserEntity.class);
        TopicEntity topic = mock(TopicEntity.class);
        CommentEntity comment = mock(CommentEntity.class);
        CommentCreateRequestDTO requestDTO = mock(CommentCreateRequestDTO.class);
        CommentResponseDTO responseDTO = mock(CommentResponseDTO.class);

        when(requestDTO.topicId()).thenReturn(topicId.toString());
        when(user.getId()).thenReturn(userId);
        when(topic.getId()).thenReturn(topicId);
        when(comment.getId()).thenReturn(commentId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
        when(user.participatingInForum(any())).thenReturn(true);
        when(commentMapper.toEntity(requestDTO, user, topic, null)).thenReturn(comment);
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);

        CommentResponseDTO result = createCommentUseCase.execute(requestDTO, userId);

        verify(commentRepository).save(comment);
        verify(topic).incrementComments();
        verify(topicRepository).save(topic);
        assertNotNull(result);
    }

    @Test
    void execute_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        CommentCreateRequestDTO requestDTO = mock(CommentCreateRequestDTO.class);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createCommentUseCase.execute(requestDTO, userId));
    }

    @Test
    void execute_ShouldThrowResourceNotFoundException_WhenTopicNotFound() {
        UUID userId = UUID.randomUUID();
        UUID topicId = UUID.randomUUID();
        UserEntity user = mock(UserEntity.class);
        CommentCreateRequestDTO requestDTO = mock(CommentCreateRequestDTO.class);

        when(requestDTO.topicId()).thenReturn(topicId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createCommentUseCase.execute(requestDTO, userId));
    }

    @Test
    void execute_ShouldThrowForbiddenException_WhenUserNotAuthorized() {
        UUID userId = UUID.randomUUID();
        UUID topicId = UUID.randomUUID();
        UserEntity user = mock(UserEntity.class);
        TopicEntity topic = mock(TopicEntity.class);
        CommentCreateRequestDTO requestDTO = mock(CommentCreateRequestDTO.class);

        when(requestDTO.topicId()).thenReturn(topicId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
        when(user.participatingInForum(any())).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> createCommentUseCase.execute(requestDTO, userId));
    }

    @Test
    void execute_ShouldThrowIllegalArgumentException_WhenParentCommentNotInTopic() {
        UUID userId = UUID.randomUUID();
        UUID topicId = UUID.randomUUID();
        UUID parentCommentId = UUID.randomUUID();
        UserEntity user = mock(UserEntity.class);
        TopicEntity topic = mock(TopicEntity.class);
        CommentEntity parentComment = mock(CommentEntity.class);
        CommentCreateRequestDTO requestDTO = mock(CommentCreateRequestDTO.class);

        when(requestDTO.topicId()).thenReturn(topicId.toString());
        when(requestDTO.parentCommentId()).thenReturn(parentCommentId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
        when(user.participatingInForum(any())).thenReturn(true);
        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.of(parentComment));
        when(parentComment.getTopic()).thenReturn(mock(TopicEntity.class));

        assertThrows(IllegalArgumentException.class, () -> createCommentUseCase.execute(requestDTO, userId));
    }
}
