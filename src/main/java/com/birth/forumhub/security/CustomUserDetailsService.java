package com.birth.forumhub.security;
import com.birth.forumhub.modules.user.entity.UserEntity;
import com.birth.forumhub.modules.user.repository.UserRepository;
import com.birth.forumhub.security.utils.JwtUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CustomUserDetailsService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }


    public String generateToken(UserEntity user) {
        return jwtUtil.generateToken(user);
    }
}
