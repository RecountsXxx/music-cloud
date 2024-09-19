package org.deus.src.repositories;

import org.deus.src.models.ReleaseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<ReleaseModel, UUID> {
}
