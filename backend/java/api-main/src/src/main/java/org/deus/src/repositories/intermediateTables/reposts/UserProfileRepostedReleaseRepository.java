package org.deus.src.repositories.intermediateTables.reposts;

import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedReleaseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepostedReleaseRepository extends JpaRepository<UserProfileRepostedReleaseModel, UUID> {
    Optional<UserProfileRepostedReleaseModel> findByUserProfileAndRelease(UserProfileModel userProfile, ReleaseModel release);
    List<UserProfileRepostedReleaseModel> findByUserProfile(UserProfileModel userProfile);
    List<UserProfileRepostedReleaseModel> findByRelease(ReleaseModel release);
}
