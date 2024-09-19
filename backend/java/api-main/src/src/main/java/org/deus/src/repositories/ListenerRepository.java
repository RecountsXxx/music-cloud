package org.deus.src.repositories;

import org.deus.src.models.ListenerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerRepository extends JpaRepository<ListenerModel, UUID> {
}
