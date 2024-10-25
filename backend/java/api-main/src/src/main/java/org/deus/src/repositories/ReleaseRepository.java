package org.deus.src.repositories;

import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<ReleaseModel, UUID> {
    Optional<ReleaseModel> findByIdAndCreatorUserProfile(UUID id, UserProfileModel creatorUserProfile);
    List<ReleaseModel> findAllByCreatorUserProfile(UserProfileModel creatorUserProfile);
    @Query("SELECT r FROM ReleaseModel r ORDER BY r.numberOfLikes DESC")
    List<ReleaseModel> findTopReleases(Pageable pageable);
}
