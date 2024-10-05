package org.deus.src.services.models.intermediateTables.likes;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedSongModel;
import org.deus.src.repositories.SongRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.likes.UserProfileLikedSongRepository;
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
public class UserProfileLikedSongService {
    private final UserProfileLikedSongRepository userProfileLikedSongRepository;
    private final UserProfileRepository userProfileRepository;
    private final SongRepository songRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles_liked_song", key = "#contentId")
    public List<ShortUserProfileDTO> getUserProfilesThatLikedContent(UUID contentId) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        return userProfileLikedSongRepository
                .findBySong(song).stream()
                .map(userProfileLikedSongModel -> getShortUserProfileDTO(userProfileLikedSongModel.getUserProfile(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "songs_liked_by_user_profile", key = "#userProfileId")
    public List<ShortSongDTO> getLikedContent(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        return userProfileLikedSongRepository
                .findByUserProfile(userProfile).stream()
                .map(userProfileLikedSongModel -> {
                    SongModel song = userProfileLikedSongModel.getSong();
                    ReleaseModel release = song.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortSongDTO(song, release, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_liked_song", key = "#contentId"),
                    @CacheEvict(value = "songs_liked_by_user_profile", key = "#userProfileId")
            }
    )
    public void likeContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        if(userProfileLikedSongRepository.findByUserProfileAndSong(userProfile, song).isPresent()) {
            throw new ActionCannotBePerformedException("Already liked this song");
        }

        UserProfileLikedSongModel userProfileLikedSong = new UserProfileLikedSongModel();
        userProfileLikedSong.setUserProfile(userProfile);
        userProfileLikedSong.setSong(song);
        userProfileLikedSongRepository.save(userProfileLikedSong);

        userProfile.setNumberOfLikedSongs(userProfile.getNumberOfLikedSongs() + 1);
        userProfileRepository.save(userProfile);

        song.setNumberOfLikes(song.getNumberOfLikes() + 1);
        songRepository.save(song);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_liked_song", key = "#contentId"),
                    @CacheEvict(value = "songs_liked_by_user_profile", key = "#userProfileId")
            }
    )
    public void removeLikeFromContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        UserProfileLikedSongModel userProfileLikedSong = userProfileLikedSongRepository
                .findByUserProfileAndSong(userProfile, song)
                .orElseThrow(() -> new ActionCannotBePerformedException("Song is not liked"));

        userProfileLikedSongRepository.delete(userProfileLikedSong);

        userProfile.setNumberOfLikedSongs(userProfile.getNumberOfLikedSongs() - 1);
        userProfileRepository.save(userProfile);

        song.setNumberOfLikes(song.getNumberOfLikes() - 1);
        songRepository.save(song);
    }
}
