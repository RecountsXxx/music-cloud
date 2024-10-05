package org.deus.src.repositories;

import org.deus.src.models.CommentModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentModel, UUID> {
    Optional<CommentModel> findByCreatorUserProfileAndSong(UserProfileModel creatorUserProfile, SongModel song);
    List<CommentModel> findBySong(SongModel song);
}
