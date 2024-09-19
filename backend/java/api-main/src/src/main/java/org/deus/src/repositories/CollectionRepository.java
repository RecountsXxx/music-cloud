package org.deus.src.repositories;

import org.deus.src.models.CollectionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CollectionRepository extends JpaRepository<CollectionModel, UUID> {
}
