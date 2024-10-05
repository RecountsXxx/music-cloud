package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.ListenerLikedSongModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerLikedSongRepository extends JpaRepository<ListenerLikedSongModel, UUID> {
}