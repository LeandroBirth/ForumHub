package com.birth.forumhub.modules.auth.usecase;

import com.birth.forumhub.modules.exception.usecase.ResourceAlreadyExistsException;
import com.birth.forumhub.modules.user.controller.dto.UserCreateRequestDTO;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.mapper.UserMapper;
import com.birth.forumhub.modules.user.repository.UserRepository;
import com.birth.forumhub.security.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SignUpUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SignUpUseCase.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieUtil cookieUtil;


    public SignUpUseCase(UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cookieUtil = cookieUtil;
    }


    public Map<String, Object> execute(UserCreateRequestDTO requestDTO) {
        validateUsernameAvailability(requestDTO.username());

        UserEntity user = createUserEntity(requestDTO);
        userRepository.save(user);

        Cookie jwtCookie = cookieUtil.generateCookieWithToken(user);

        logger.info("User {} registered successfully", requestDTO.username());

        return Map.of(
                "user", UserMapper.toDetailsResponseDTO(user),
                "cookie", jwtCookie
        );
    }


    private void validateUsernameAvailability(String username) {
        if (userRepository.existsByUsername(username)) {
            logger.warn("Attempt to register with existing username: {}", username);
            throw new ResourceAlreadyExistsException("Username already exists");
        }
    }

    private UserEntity createUserEntity(UserCreateRequestDTO requestDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(requestDTO.username());
        user.setPassword(passwordEncoder.encode(requestDTO.password()));
        user.setName(requestDTO.name());
        user.setEmail(requestDTO.email());

        return user;
    }
}
