package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.ListenerLikedPlaylistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerLikedPlaylistRepository extends JpaRepository<ListenerLikedPlaylistModel, UUID> {
}
