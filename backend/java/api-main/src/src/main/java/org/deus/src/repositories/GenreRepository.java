package org.deus.src.repositories;

import org.deus.src.models.GenreModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreModel, Short> {
}
