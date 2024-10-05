package org.deus.src.repositories.intermediateTables.likes;

import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedSongModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileLikedSongRepository extends JpaRepository<UserProfileLikedSongModel, UUID> {
    Optional<UserProfileLikedSongModel> findByUserProfileAndSong(UserProfileModel userProfile, SongModel song);
    List<UserProfileLikedSongModel> findByUserProfile(UserProfileModel userProfile);
    List<UserProfileLikedSongModel> findBySong(SongModel song);
}
