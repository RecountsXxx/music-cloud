package org.deus.src.repositories.intermediateTables.likes;

import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedReleaseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileLikedReleaseRepository extends JpaRepository<UserProfileLikedReleaseModel, UUID> {
    Optional<UserProfileLikedReleaseModel> findByUserProfileAndRelease(UserProfileModel userProfile, ReleaseModel release);
    List<UserProfileLikedReleaseModel> findByUserProfile(UserProfileModel userProfile);
    List<UserProfileLikedReleaseModel> findByRelease(ReleaseModel release);
}
