package org.deus.src.repositories.intermediateTables.reposts;

import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedSongModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepostedSongRepository extends JpaRepository<UserProfileRepostedSongModel, UUID> {
    Optional<UserProfileRepostedSongModel> findByUserProfileAndSong(UserProfileModel userProfile, SongModel song);
    List<UserProfileRepostedSongModel> findByUserProfile(UserProfileModel userProfile);
    List<UserProfileRepostedSongModel> findBySong(SongModel song);
}
