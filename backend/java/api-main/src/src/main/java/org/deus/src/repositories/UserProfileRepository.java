package org.deus.src.repositories;

import org.deus.src.models.UserProfileModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfileModel, UUID> {
    Optional<UserProfileModel> findByUserId(UUID userId);
    Optional<UserProfileModel> findByUsername(String username);
    Optional<UserProfileModel> findByIdAndUserId(UUID id, UUID userId);
    Boolean existsByIdAndUserId(UUID id, UUID userId);
    @Query(value = "SELECT u FROM UserProfileModel u ORDER BY u.monthlyListeners DESC")
    List<UserProfileModel> findTopArtists(Pageable pageable);
}
