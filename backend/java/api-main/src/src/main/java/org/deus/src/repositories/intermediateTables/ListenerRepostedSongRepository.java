package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.ListenerRepostedSongModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerRepostedSongRepository extends JpaRepository<ListenerRepostedSongModel, UUID> {
}
