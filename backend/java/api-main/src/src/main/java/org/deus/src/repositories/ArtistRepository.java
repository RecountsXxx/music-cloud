package org.deus.src.repositories;

import org.deus.src.models.ArtistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistRepository extends JpaRepository<ArtistModel, UUID> {
}
