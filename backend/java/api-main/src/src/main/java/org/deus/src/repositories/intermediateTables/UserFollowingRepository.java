package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.UserFollowingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFollowingRepository extends JpaRepository<UserFollowingModel, UUID> {
    Optional<UserFollowingModel> findByFollowerAndFollowing(UserProfileModel follower, UserProfileModel following);
    List<UserFollowingModel> findByFollower(UserProfileModel follower);
    List<UserFollowingModel> findByFollowing(UserProfileModel following);
}
