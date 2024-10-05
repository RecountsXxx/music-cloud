package org.deus.src.repositories;

import org.deus.src.models.UserProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfileModel, UUID> {
    Optional<UserProfileModel> findByUserId(UUID userId);
}
