package org.deus.src.repositories;

import org.deus.src.models.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<TagModel, UUID> {
    Optional<TagModel> findByName(String name);
}