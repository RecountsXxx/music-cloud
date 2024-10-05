package org.deus.src.services.models.intermediateTables.reposts;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedSongModel;
import org.deus.src.repositories.SongRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.reposts.UserProfileRepostedSongRepository;
import org.deus.src.services.ImageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.SongService.getShortSongDTO;
import static org.deus.src.services.models.UserProfileService.getShortUserProfileDTO;

@Service
@RequiredArgsConstructor
public class UserProfileRepostedSongService {
    private final UserProfileRepostedSongRepository userProfileRepostedSongRepository;
    private final UserProfileRepository userProfileRepository;
    private final SongRepository songRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles_reposted_song", key = "#contentId")
    public List<ShortUserProfileDTO> getUserProfilesThatRepostedContent(UUID contentId) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        return userProfileRepostedSongRepository
                .findBySong(song).stream()
                .map(userProfileRepostedSongModel -> getShortUserProfileDTO(userProfileRepostedSongModel.getUserProfile(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "songs_reposted_by_user_profile", key = "#userProfileId")
    public List<ShortSongDTO> getRepostedContent(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        return userProfileRepostedSongRepository
                .findByUserProfile(userProfile).stream()
                .map(userProfileRepostedSongModel -> {
                    SongModel song = userProfileRepostedSongModel.getSong();
                    ReleaseModel release = song.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortSongDTO(song, release, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_reposted_song", key = "#contentId"),
                    @CacheEvict(value = "songs_reposted_by_user_profile", key = "#userProfileId")
            }
    )
    public void repostContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        if(userProfileRepostedSongRepository.findByUserProfileAndSong(userProfile, song).isPresent()) {
            throw new ActionCannotBePerformedException("Already reposted this song");
        }

        UserProfileRepostedSongModel userProfileRepostedSong = new UserProfileRepostedSongModel();
        userProfileRepostedSong.setUserProfile(userProfile);
        userProfileRepostedSong.setSong(song);
        userProfileRepostedSongRepository.save(userProfileRepostedSong);

        userProfile.setNumberOfRepostedSongs(userProfile.getNumberOfRepostedSongs() + 1);
        userProfileRepository.save(userProfile);

        song.setNumberOfReposts(song.getNumberOfReposts() + 1);
        songRepository.save(song);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_reposted_song", key = "#contentId"),
                    @CacheEvict(value = "songs_reposted_by_user_profile", key = "#userProfileId")
            }
    )
    public void removeRepostOfContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        UserProfileRepostedSongModel userProfileRepostedSong = userProfileRepostedSongRepository
                .findByUserProfileAndSong(userProfile, song)
                .orElseThrow(() -> new ActionCannotBePerformedException("Song is not reposted"));

        userProfileRepostedSongRepository.delete(userProfileRepostedSong);

        userProfile.setNumberOfRepostedSongs(userProfile.getNumberOfRepostedSongs() - 1);
        userProfileRepository.save(userProfile);

        song.setNumberOfReposts(song.getNumberOfReposts() - 1);
        songRepository.save(song);
    }
}
