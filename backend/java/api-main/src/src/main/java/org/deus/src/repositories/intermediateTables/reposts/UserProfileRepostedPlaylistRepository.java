package org.deus.src.repositories.intermediateTables.reposts;

import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedPlaylistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepostedPlaylistRepository extends JpaRepository<UserProfileRepostedPlaylistModel, UUID> {
    Optional<UserProfileRepostedPlaylistModel> findByUserProfileAndPlaylist(UserProfileModel userProfile, PlaylistModel playlist);
    List<UserProfileRepostedPlaylistModel> findByUserProfile(UserProfileModel userProfile);
    List<UserProfileRepostedPlaylistModel> findByPlaylist(PlaylistModel playlist);
}
