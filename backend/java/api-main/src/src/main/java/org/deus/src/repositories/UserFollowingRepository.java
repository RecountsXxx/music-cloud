package org.deus.src.repositories;

import org.deus.src.models.UserFollowingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserFollowingRepository extends JpaRepository<UserFollowingModel, UUID> {
}
