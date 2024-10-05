package org.deus.src.services.models.intermediateTables;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.song.SongListenedDTO;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.UserProfileListenedHistoryModel;
import org.deus.src.repositories.ReleaseRepository;
import org.deus.src.repositories.SongRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.UserProfileListenedHistoryRepository;
import org.deus.src.services.ImageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.ReleaseService.getShortReleaseDTO;
import static org.deus.src.services.models.SongService.getShortSongDTO;

@Service
@RequiredArgsConstructor
public class UserProfileListenedHistoryService {
    private final UserProfileListenedHistoryRepository userProfileListenedHistoryRepository;
    private final UserProfileRepository userProfileRepository;
    private final SongRepository songRepository;
    private final ReleaseRepository releaseRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "songs_listened_history_of_user_profile", key = "#userProfileId")
    public List<SongListenedDTO> getSongsListenedHistory(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        return userProfileListenedHistoryRepository
                .findByUserProfile(userProfile).stream()
                .map(userProfileListenedHistoryModel -> {
                    SongModel song = userProfileListenedHistoryModel.getSong();
                    ReleaseModel release = song.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    ShortSongDTO songDTO = getShortSongDTO(song, release, creatorUserProfile, imageService);

                    return new SongListenedDTO(songDTO, userProfileListenedHistoryModel.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "songs_listened_history_of_user_profile", key = "#userProfileId")
            }
    )
    public void addSongToHistory(UUID userProfileId, UUID songId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        UserProfileListenedHistoryModel userProfileListenedHistoryModel = new UserProfileListenedHistoryModel();
        userProfileListenedHistoryModel.setUserProfile(userProfile);
        userProfileListenedHistoryModel.setSong(song);
        userProfileListenedHistoryRepository.save(userProfileListenedHistoryModel);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "songs_listened_history_of_user_profile", key = "#userProfileId")
            }
    )
    public void clearHistory(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        userProfileListenedHistoryRepository.deleteAllByUserProfile(userProfile);
    }
}
