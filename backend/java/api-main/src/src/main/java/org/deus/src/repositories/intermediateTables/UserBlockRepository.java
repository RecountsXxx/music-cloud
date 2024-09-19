package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.UserBlockModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserBlockRepository extends JpaRepository<UserBlockModel, UUID> {
}
