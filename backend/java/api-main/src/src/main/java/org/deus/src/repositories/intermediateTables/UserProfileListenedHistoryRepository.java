package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.UserProfileListenedHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileListenedHistoryRepository extends JpaRepository<UserProfileListenedHistoryModel, UUID> {
    Optional<UserProfileListenedHistoryModel> findByUserProfileAndSong(UserProfileModel userProfile, SongModel song);
    List<UserProfileListenedHistoryModel> findByUserProfile(UserProfileModel userProfile);
    void deleteAllByUserProfile(UserProfileModel userProfile);
}
