package org.deus.src.services.models.intermediateTables.likes;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedPlaylistModel;
import org.deus.src.repositories.PlaylistRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.likes.UserProfileLikedPlaylistRepository;
import org.deus.src.services.ImageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.PlaylistService.getShortPlaylistDTO;
import static org.deus.src.services.models.UserProfileService.getShortUserProfileDTO;

@Service
@RequiredArgsConstructor
public class UserProfileLikedPlaylistService {
    private final UserProfileLikedPlaylistRepository userProfileLikedPlaylistRepository;
    private final UserProfileRepository userProfileRepository;
    private final PlaylistRepository playlistRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles_liked_playlist", key = "#contentId")
    public List<ShortUserProfileDTO> getUserProfilesThatLikedContent(UUID contentId) throws DataNotFoundException {
        PlaylistModel playlist = playlistRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        return userProfileLikedPlaylistRepository
                .findByPlaylist(playlist).stream()
                .map(userProfileLikedPlaylistModel -> getShortUserProfileDTO(userProfileLikedPlaylistModel.getUserProfile(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "playlists_liked_by_user_profile", key = "#userProfileId")
    public List<ShortPlaylistDTO> getLikedContent(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        return userProfileLikedPlaylistRepository
                .findByUserProfile(userProfile).stream()
                .map(userProfileLikedPlaylistModel -> {
                    PlaylistModel playlist = userProfileLikedPlaylistModel.getPlaylist();
                    UserProfileModel creatorUserProfile = playlist.getCreatorUserProfile();

                    return getShortPlaylistDTO(playlist, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_liked_playlist", key = "#contentId"),
                    @CacheEvict(value = "playlists_liked_by_user_profile", key = "#userProfileId")
            }
    )
    public void likeContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        PlaylistModel playlist = playlistRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        if(userProfileLikedPlaylistRepository.findByUserProfileAndPlaylist(userProfile, playlist).isPresent()) {
            throw new ActionCannotBePerformedException("Already liked this playlist");
        }

        UserProfileLikedPlaylistModel userProfileLikedPlaylist = new UserProfileLikedPlaylistModel();
        userProfileLikedPlaylist.setUserProfile(userProfile);
        userProfileLikedPlaylist.setPlaylist(playlist);
        userProfileLikedPlaylistRepository.save(userProfileLikedPlaylist);

        userProfile.setNumberOfLikedPlaylists(userProfile.getNumberOfLikedPlaylists() + 1);
        userProfileRepository.save(userProfile);

        playlist.setNumberOfLikes(playlist.getNumberOfLikes() + 1);
        playlistRepository.save(playlist);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_liked_playlist", key = "#contentId"),
                    @CacheEvict(value = "playlists_liked_by_user_profile", key = "#userProfileId")
            }
    )
    public void removeLikeFromContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        PlaylistModel playlist = playlistRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        UserProfileLikedPlaylistModel userProfileLikedPlaylist = userProfileLikedPlaylistRepository
                .findByUserProfileAndPlaylist(userProfile, playlist)
                .orElseThrow(() -> new ActionCannotBePerformedException("Playlist is not liked"));

        userProfileLikedPlaylistRepository.delete(userProfileLikedPlaylist);

        userProfile.setNumberOfLikedPlaylists(userProfile.getNumberOfLikedPlaylists() - 1);
        userProfileRepository.save(userProfile);

        playlist.setNumberOfLikes(playlist.getNumberOfLikes() - 1);
        playlistRepository.save(playlist);
    }
}
