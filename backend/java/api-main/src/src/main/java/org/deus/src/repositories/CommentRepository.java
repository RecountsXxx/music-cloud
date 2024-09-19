package org.deus.src.repositories;

import org.deus.src.models.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentModel, UUID> {
}
