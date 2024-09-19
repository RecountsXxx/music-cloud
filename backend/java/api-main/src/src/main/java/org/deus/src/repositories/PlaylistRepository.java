package org.deus.src.repositories;

import org.deus.src.models.PlaylistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<PlaylistModel, UUID> {
}
