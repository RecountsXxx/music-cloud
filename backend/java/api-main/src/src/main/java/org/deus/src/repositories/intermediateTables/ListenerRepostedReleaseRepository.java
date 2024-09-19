package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.ListenerRepostedReleaseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerRepostedReleaseRepository extends JpaRepository<ListenerRepostedReleaseModel, UUID> {
}
