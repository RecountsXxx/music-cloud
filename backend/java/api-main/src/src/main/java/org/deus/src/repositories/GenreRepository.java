package org.deus.src.repositories;

import org.deus.src.models.GenreModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreModel, Short> {
    @Query("SELECT g FROM GenreModel g JOIN g.songs s GROUP BY g ORDER BY SUM(s.numberOfPlays) DESC")
    List<GenreModel> findTopGenres(Pageable pageable);
}
