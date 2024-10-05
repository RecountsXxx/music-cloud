package org.deus.src.repositories.intermediateTables.likes;

import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedPlaylistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileLikedPlaylistRepository extends JpaRepository<UserProfileLikedPlaylistModel, UUID> {
    Optional<UserProfileLikedPlaylistModel> findByUserProfileAndPlaylist(UserProfileModel userProfile, PlaylistModel playlist);
    List<UserProfileLikedPlaylistModel> findByUserProfile(UserProfileModel userProfile);
    List<UserProfileLikedPlaylistModel> findByPlaylist(PlaylistModel playlist);
}
