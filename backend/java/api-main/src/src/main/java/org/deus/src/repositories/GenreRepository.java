package org.deus.src.repositories;

import org.deus.src.models.GenreModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GenreRepository extends JpaRepository<GenreModel, Short> {
}
