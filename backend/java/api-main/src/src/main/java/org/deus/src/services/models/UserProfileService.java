package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.song.SongListenedDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileDTO;
import org.deus.src.enums.AudioQuality;
import org.deus.src.enums.LikeType;
import org.deus.src.enums.RepostType;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.*;
import org.deus.src.repositories.PlaylistRepository;
import org.deus.src.repositories.ReleaseRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.requests.userProfile.UserProfileCreateRequest;
import org.deus.src.requests.userProfile.UserProfileUpdateRequest;
import org.deus.src.services.ImageService;
import org.deus.src.services.models.intermediateTables.UserBlockService;
import org.deus.src.services.models.intermediateTables.UserFollowingService;
import org.deus.src.services.models.intermediateTables.UserProfileListenedHistoryService;
import org.deus.src.services.models.intermediateTables.likes.UserProfileLikedPlaylistService;
import org.deus.src.services.models.intermediateTables.likes.UserProfileLikedReleaseService;
import org.deus.src.services.models.intermediateTables.likes.UserProfileLikedSongService;
import org.deus.src.services.models.intermediateTables.reposts.UserProfileRepostedPlaylistService;
import org.deus.src.services.models.intermediateTables.reposts.UserProfileRepostedReleaseService;
import org.deus.src.services.models.intermediateTables.reposts.UserProfileRepostedSongService;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.PlaylistService.getShortPlaylistDTO;
import static org.deus.src.services.models.ReleaseService.getShortReleaseDTO;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final ReleaseRepository releaseRepository;
    private final PlaylistRepository playlistRepository;
    private final CountryService countryService;
    private final UserProfileLikedReleaseService userProfileLikedReleaseService;
    private final UserProfileLikedPlaylistService userProfileLikedPlaylistService;
    private final UserProfileLikedSongService userProfileLikedSongService;
    private final UserProfileRepostedReleaseService userProfileRepostedReleaseService;
    private final UserProfileRepostedPlaylistService userProfileRepostedPlaylistService;
    private final UserProfileRepostedSongService userProfileRepostedSongService;
    private final UserProfileListenedHistoryService userProfileListenedHistoryService;
    private final UserFollowingService userFollowingService;
    private final UserBlockService userBlockService;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles_pageable", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ShortUserProfileDTO> getAll(Pageable pageable) {
        return userProfileRepository
                .findAll(pageable)
                .map(userProfile -> getShortUserProfileDTO(userProfile, imageService));
    }

    @Cacheable(value = "user_profile_by_id", key = "#id")
    public UserProfileModel getById(UUID id) throws DataNotFoundException {
        return userProfileRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User profile not found"));
    }

    @Cacheable(value = "user_profile_by_user_id", key = "#userId")
    public UserProfileModel getByUserId(UUID userId) throws DataNotFoundException {
        return userProfileRepository.findByUserId(userId).orElseThrow(() -> new DataNotFoundException("User profile not found"));
    }

    @Cacheable(value = "user_profile_by_id_dto", key = "#id")
    public UserProfileDTO getDTOById(UUID id) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User profile not found"));

        return getUserProfileDTO(userProfile, imageService);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"user_profiles_pageable"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "user_profile_by_id", key = "#result.id"),
                    @CachePut(value = "user_profile_by_user_id", key = "#result.userId")
            }
    )
    public UserProfileModel create(UserProfileCreateRequest request) {
        UserProfileModel userProfile = new UserProfileModel();

        userProfile.setUserId(UUID.fromString(request.getUserId()));
        userProfile.setDisplayName(request.getDisplayName());
        userProfile.setPreferredQuality(AudioQuality.MEDIUM);

        return userProfileRepository.save(userProfile);
    }
    
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"user_profiles_pageable", "user_profiles_liked_playlist", "user_profiles_liked_release", "user_profiles_liked_song", "user_profiles_reposted_playlist", "user_profiles_reposted_release", "user_profiles_reposted_song"}, allEntries = true),
                    @CacheEvict(value = "user_profile_by_id_dto", key = "#result.id")
            },
            put = {
                    @CachePut(value = "user_profile_by_id", key = "#result.id"),
                    @CachePut(value = "user_profile_by_user_id", key = "#result.userId")
            }
    )
    public UserProfileModel update(UserProfileUpdateRequest request) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new DataNotFoundException("User profile not found"));

        if (request.getDisplayName() != null) {
            userProfile.setDisplayName(request.getDisplayName());
        }
        if (request.getFirstName() != null) {
            userProfile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            userProfile.setLastName(request.getLastName());
        }
        if (request.getBirthday() != null) {
            userProfile.setBirthday(request.getBirthday());
        }
        if (request.getGender() != null) {
            userProfile.setGender(request.getGender());
        }
        if (request.getPreferredQuality() != null) {
            userProfile.setPreferredQuality(request.getPreferredQuality());
        }
        if (request.getBiography() != null) {
            userProfile.setBiography(request.getBiography());
        }
        if (request.getCountryId() != null) {
            CountryModel country = countryService.getById(request.getCountryId());
            userProfile.setCountry(country);
        }

        return userProfileRepository.save(userProfile);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"user_profiles_pageable", "user_profiles_liked_playlist", "user_profiles_liked_release", "user_profiles_liked_song", "user_profiles_reposted_playlist", "user_profiles_reposted_release", "user_profiles_reposted_song"}, allEntries = true),
                    @CacheEvict(value = {"user_profile_by_id", "user_profile_by_id_dto"}, key = "#id")
            }
    )
    public void delete(UUID id) {
        userProfileRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "releases_by_creator_user_profile", key = "#creatorUserProfileId")
    public List<ShortReleaseDTO> getReleasesByCreatorUserProfileId(UUID creatorUserProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = getById(creatorUserProfileId);

        return releaseRepository
                .findAllByCreatorUserProfile(userProfile).stream()
                .map(release -> {
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortReleaseDTO(release, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "playlists_by_creator_user_profile", key = "#creatorUserProfileId")
    public List<ShortPlaylistDTO> getPlaylistsByCreatorUserProfileId(UUID creatorUserProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = getById(creatorUserProfileId);

        return playlistRepository
                .findAllByCreatorUserProfile(userProfile).stream()
                .map(playlist -> {
                    UserProfileModel creatorUserProfile = playlist.getCreatorUserProfile();

                    return getShortPlaylistDTO(playlist, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }



    public void likeContent(UUID userProfileId, UUID contentId, LikeType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileLikedReleaseService.likeContent(userProfileId, contentId);
            case PLAYLIST -> userProfileLikedPlaylistService.likeContent(userProfileId, contentId);
            case SONG -> userProfileLikedSongService.likeContent(userProfileId, contentId);
        }
    }
    public void removeLikeFromContent(UUID userProfileId, UUID contentId, LikeType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileLikedReleaseService.removeLikeFromContent(userProfileId, contentId);
            case PLAYLIST -> userProfileLikedPlaylistService.removeLikeFromContent(userProfileId, contentId);
            case SONG -> userProfileLikedSongService.removeLikeFromContent(userProfileId, contentId);
        }
    }
    public List<ShortReleaseDTO> getLikedReleases(UUID userProfileId) throws DataNotFoundException {
        return userProfileLikedReleaseService.getLikedContent(userProfileId);
    }
    public List<ShortPlaylistDTO> getLikedPlaylists(UUID userProfileId) throws DataNotFoundException {
        return userProfileLikedPlaylistService.getLikedContent(userProfileId);
    }
    public List<ShortSongDTO> getLikedSongs(UUID userProfileId) throws DataNotFoundException {
        return userProfileLikedSongService.getLikedContent(userProfileId);
    }



    public void repostContent(UUID userProfileId, UUID contentId, RepostType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileRepostedReleaseService.repostContent(userProfileId, contentId);
            case PLAYLIST -> userProfileRepostedPlaylistService.repostContent(userProfileId, contentId);
            case SONG -> userProfileRepostedSongService.repostContent(userProfileId, contentId);
        }
    }
    public void removeRepostFromContent(UUID userProfileId, UUID contentId, RepostType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileRepostedReleaseService.removeRepostOfContent(userProfileId, contentId);
            case PLAYLIST -> userProfileRepostedPlaylistService.removeRepostOfContent(userProfileId, contentId);
            case SONG -> userProfileRepostedSongService.removeRepostOfContent(userProfileId, contentId);
        }
    }
    public List<ShortReleaseDTO> getRepostedReleases(UUID userProfileId) throws DataNotFoundException {
        return userProfileRepostedReleaseService.getRepostedContent(userProfileId);
    }
    public List<ShortPlaylistDTO> getRepostedPlaylists(UUID userProfileId) throws DataNotFoundException {
        return userProfileRepostedPlaylistService.getRepostedContent(userProfileId);
    }
    public List<ShortSongDTO> getRepostedSongs(UUID userProfileId) throws DataNotFoundException {
        return userProfileRepostedSongService.getRepostedContent(userProfileId);
    }



    public void addSongToHistory(UUID userProfileId, UUID songId) throws DataNotFoundException {
        userProfileListenedHistoryService.addSongToHistory(userProfileId, songId);
    }
    public void clearHistory(UUID userProfileId) throws DataNotFoundException {
        userProfileListenedHistoryService.clearHistory(userProfileId);
    }
    public List<SongListenedDTO> getSongsListenedHistory(UUID userProfileId) throws DataNotFoundException {
        return userProfileListenedHistoryService.getSongsListenedHistory(userProfileId);
    }


    
    public void followUser(UUID followerId, UUID followingId) throws DataNotFoundException, ActionCannotBePerformedException {
        userFollowingService.followUser(followerId, followingId);
    }
    public void unfollowUser(UUID followerId, UUID followingId) throws DataNotFoundException, ActionCannotBePerformedException {
        userFollowingService.unfollowUser(followerId, followingId);
    }
    public List<ShortUserProfileDTO> getFollowers(UUID id) throws DataNotFoundException {
        return userFollowingService.getFollowers(id);
    }
    public List<ShortUserProfileDTO> getFollowings(UUID id) throws DataNotFoundException {
        return userFollowingService.getFollowings(id);
    }
    public List<ShortUserProfileDTO> getBlockedUsers(UUID id) throws DataNotFoundException {
        return userBlockService.getBlockedUsers(id);
    }



    @NotNull
    public static ShortUserProfileDTO getShortUserProfileDTO(UserProfileModel userProfile, ImageService imageService) {
        ImageUrlsDTO avatar = imageService.getAvatarForUser(userProfile.getUserId().toString());

        return UserProfileModel.toShortDTO(userProfile, avatar);
    }

    @NotNull
    public static UserProfileDTO getUserProfileDTO(UserProfileModel userProfile, ImageService imageService) {
        ImageUrlsDTO avatar = imageService.getAvatarForUser(userProfile.getUserId().toString());

        return UserProfileModel.toDTO(userProfile, avatar);
    }
}
