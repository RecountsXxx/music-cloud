package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.UserBlockModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserBlockRepository extends JpaRepository<UserBlockModel, UUID> {
    Optional<UserBlockModel> findByBlockerAndBlocked(UserProfileModel blocker, UserProfileModel blocked);
    List<UserBlockModel> findByBlocker(UserProfileModel blocker);
}
