package com.birth.forumhub.modules.user.repository;

import com.birth.forumhub.modules.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
