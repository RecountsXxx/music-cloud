package org.deus.src.repositories;

import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<PlaylistModel, UUID> {
    Optional<PlaylistModel> findByIdAndCreatorUserProfile(UUID id, UserProfileModel creatorUserProfile);
    List<PlaylistModel> findAllByCreatorUserProfile(UserProfileModel creatorUserProfile);

    @Query("SELECT p FROM PlaylistModel p ORDER BY p.numberOfLikes DESC")
    List<PlaylistModel> findTopPlaylists(Pageable pageable);
}
