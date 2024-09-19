package org.deus.src.repositories;

import org.deus.src.models.SongModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SongRepository extends JpaRepository<SongModel, UUID> {
}
