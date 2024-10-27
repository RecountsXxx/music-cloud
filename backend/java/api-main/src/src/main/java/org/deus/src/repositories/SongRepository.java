package org.deus.src.repositories;

import org.deus.src.models.GenreModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.TagModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SongRepository extends JpaRepository<SongModel, UUID> {
    @Query("SELECT g FROM SongModel s JOIN s.genres g WHERE s.id = :songId")
    List<GenreModel> findGenresBySongId(@Param("songId") UUID songId);
    @Query("SELECT t FROM SongModel s JOIN s.tags t WHERE s.id = :songId")
    List<TagModel> findTagsBySongId(@Param("songId") UUID songId);
    @Query("SELECT s FROM SongModel s ORDER BY s.numberOfPlays DESC")
    List<SongModel> findTopSongs(Pageable pageable);
}
