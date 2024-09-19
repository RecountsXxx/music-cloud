package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.intermediateTables.ListenerListenedHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListenerListenedHistoryRepository extends JpaRepository<ListenerListenedHistoryModel, UUID> {
}
