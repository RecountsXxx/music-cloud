package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.ListenerLikedReleaseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerLikedReleaseRepository extends JpaRepository<ListenerLikedReleaseModel, UUID> {
}
