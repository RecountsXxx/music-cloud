package org.deus.src.repositories;

import org.deus.src.models.SongModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SongRepository extends JpaRepository<SongModel, UUID> {
    @Query("SELECT s FROM SongModel s ORDER BY s.numberOfPlays DESC")
    List<SongModel> findTopSongs(Pageable pageable);
}
