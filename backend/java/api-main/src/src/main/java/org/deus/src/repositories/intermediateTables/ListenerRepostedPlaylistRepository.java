package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.ListenerRepostedPlaylistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerRepostedPlaylistRepository extends JpaRepository<ListenerRepostedPlaylistModel, UUID> {
}
