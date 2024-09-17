package org.deus.src.repositories;

import org.deus.src.models.UserBlockModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserBlockRepository extends JpaRepository<UserBlockModel, UUID> {
}
