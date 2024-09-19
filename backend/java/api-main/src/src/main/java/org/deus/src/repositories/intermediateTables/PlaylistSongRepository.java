package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.PlaylistSongModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSongModel, UUID> {
}
