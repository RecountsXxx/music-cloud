package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.UserFollowingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserFollowingRepository extends JpaRepository<UserFollowingModel, UUID> {
}
