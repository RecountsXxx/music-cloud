package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.PageDTO;
import org.deus.src.dtos.actions.LikeContentDTO;
import org.deus.src.dtos.actions.RepostContentDTO;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.song.SongListenedDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileActionDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.PublicUserProfileDTO;
import org.deus.src.enums.AudioQuality;
import org.deus.src.enums.ContentType;
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
import org.springframework.data.domain.PageRequest;
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
    public PageDTO<ShortUserProfileDTO> getAll(Pageable pageable) {
        Page<ShortUserProfileDTO> page = userProfileRepository
                .findAll(pageable)
                .map(userProfile -> getShortUserProfileDTO(userProfile, imageService));

        return new PageDTO<>(page);
    }

    @Cacheable(value = "user_profile_by_id", key = "#id")
    public UserProfileModel getById(UUID id) throws DataNotFoundException {
        return userProfileRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User profile not found"));
    }

    @Cacheable(value = "user_profile_by_user_id", key = "#userId")
    public UserProfileModel getByUserId(UUID userId) throws DataNotFoundException {
        return userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("User profile not found"));
    }

    public Boolean existsByIdAndUserId(UUID id, UUID userId) throws ActionCannotBePerformedException {
        return userProfileRepository.existsByIdAndUserId(id, userId);
    }

    @Cacheable(value = "user_profile_by_username", key = "#username")
    public UserProfileModel getByUsername(String username) throws DataNotFoundException {
        return userProfileRepository
                .findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User profile not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profile_by_id_dto", key = "#id")
    public UserProfileDTO getDTOById(UUID id) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User profile not found"));

        Short countryId = userProfile.getCountry() != null ?
                userProfile.getCountry().getId() :
                null;

        return getUserProfileDTO(userProfile, imageService, countryId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "public_user_profile_by_id_dto", key = "#id")
    public PublicUserProfileDTO getPublicDTOById(UUID id) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User profile not found"));

        String countryName = userProfile.getCountry() != null ?
                userProfile.getCountry().getName() :
                null;

        return getPublicUserProfileDTO(userProfile, imageService, countryName);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"user_profiles_pageable"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "user_profile_by_id", key = "#result.id"),
                    @CachePut(value = "user_profile_by_user_id", key = "#userId")
            }
    )
    public UserProfileModel create(UserProfileCreateRequest request, UUID userId) {
        UserProfileModel userProfile = new UserProfileModel();

        userProfile.setUserId(userId);
        userProfile.setUsername(request.getUsername());
        userProfile.setDisplayName(request.getDisplayName());
        userProfile.setPreferredQuality(AudioQuality.MEDIUM);

        return userProfileRepository.save(userProfile);
    }
    
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"user_profiles_pageable", "user_profiles_liked_playlist", "user_profiles_liked_release", "user_profiles_liked_song", "user_profiles_reposted_playlist", "user_profiles_reposted_release", "user_profiles_reposted_song"}, allEntries = true),
                    @CacheEvict(value = {"user_profile_by_id_dto", "public_user_profile_by_id_dto"}, key = "#userProfileId")
            },
            put = {
                    @CachePut(value = "user_profile_by_id", key = "#userProfileId"),
                    @CachePut(value = "user_profile_by_user_id", key = "#userId")
            }
    )
    public UserProfileModel update(UserProfileUpdateRequest request, UUID userProfileId, UUID userId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository.findByIdAndUserId(userProfileId, userId)
                .orElseThrow(() -> new ActionCannotBePerformedException("User profile not found or you don't have permission to update it"));

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
                    @CacheEvict(value = {"user_profile_by_id", "user_profile_by_id_dto", "public_user_profile_by_id_dto"}, key = "#id"),
                    @CacheEvict(value = {"user_profile_by_user_id"}, key = "#userId")
            }
    )
    public void delete(UUID id, UUID userId) throws ActionCannotBePerformedException {
        if (!existsByIdAndUserId(id, userId)) {
            throw new ActionCannotBePerformedException("User profile not found or you don't have permission to delete it");
        }

        userProfileRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "releases_by_creator_user_profile", key = "#creatorUserProfileId")
    public List<ShortReleaseDTO> getReleasesByCreatorUserProfileId(UUID creatorUserProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = getById(creatorUserProfileId);

        return releaseRepository
                .findAllByCreatorUserProfile(userProfile).stream()
                .map(release -> getShortReleaseDTO(release, userProfile, imageService))
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



    public void likeContent(UUID userProfileId, UUID contentId, ContentType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileLikedReleaseService.likeContent(userProfileId, contentId);
            case PLAYLIST -> userProfileLikedPlaylistService.likeContent(userProfileId, contentId);
            case SONG -> userProfileLikedSongService.likeContent(userProfileId, contentId);
        }
    }
    public void removeLikeFromContent(UUID userProfileId, UUID contentId, ContentType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileLikedReleaseService.removeLikeFromContent(userProfileId, contentId);
            case PLAYLIST -> userProfileLikedPlaylistService.removeLikeFromContent(userProfileId, contentId);
            case SONG -> userProfileLikedSongService.removeLikeFromContent(userProfileId, contentId);
        }
    }
    public List<LikeContentDTO> getLikedReleases(UUID userProfileId) throws DataNotFoundException {
        return userProfileLikedReleaseService.getLikedContent(userProfileId);
    }
    public List<LikeContentDTO> getLikedPlaylists(UUID userProfileId) throws DataNotFoundException {
        return userProfileLikedPlaylistService.getLikedContent(userProfileId);
    }
    public List<LikeContentDTO> getLikedSongs(UUID userProfileId) throws DataNotFoundException {
        return userProfileLikedSongService.getLikedContent(userProfileId);
    }



    public void repostContent(UUID userProfileId, UUID contentId, ContentType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileRepostedReleaseService.repostContent(userProfileId, contentId);
            case PLAYLIST -> userProfileRepostedPlaylistService.repostContent(userProfileId, contentId);
            case SONG -> userProfileRepostedSongService.repostContent(userProfileId, contentId);
        }
    }
    public void removeRepostFromContent(UUID userProfileId, UUID contentId, ContentType contentType) throws DataNotFoundException, ActionCannotBePerformedException {
        switch (contentType) {
            case RELEASE -> userProfileRepostedReleaseService.removeRepostOfContent(userProfileId, contentId);
            case PLAYLIST -> userProfileRepostedPlaylistService.removeRepostOfContent(userProfileId, contentId);
            case SONG -> userProfileRepostedSongService.removeRepostOfContent(userProfileId, contentId);
        }
    }
    public List<RepostContentDTO> getRepostedReleases(UUID userProfileId) throws DataNotFoundException {
        return userProfileRepostedReleaseService.getRepostedContent(userProfileId);
    }
    public List<RepostContentDTO> getRepostedPlaylists(UUID userProfileId) throws DataNotFoundException {
        return userProfileRepostedPlaylistService.getRepostedContent(userProfileId);
    }
    public List<RepostContentDTO> getRepostedSongs(UUID userProfileId) throws DataNotFoundException {
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
    public List<UserProfileActionDTO> getFollowers(UUID id) throws DataNotFoundException {
        return userFollowingService.getFollowers(id);
    }
    public List<UserProfileActionDTO> getFollowings(UUID id) throws DataNotFoundException {
        return userFollowingService.getFollowings(id);
    }
    public void blockUser(UUID blockerId, UUID blockedId) throws DataNotFoundException, ActionCannotBePerformedException {
        userBlockService.blockUser(blockerId, blockedId);
    }
    public void unblockUser(UUID blockerId, UUID blockedId) throws DataNotFoundException, ActionCannotBePerformedException {
        userBlockService.unblockUser(blockerId, blockedId);
    }
    public List<UserProfileActionDTO> getBlockedUsers(UUID id) throws DataNotFoundException {
        return userBlockService.getBlockedUsers(id);
    }



    @Transactional(readOnly = true)
    @Cacheable(value = "top_artists", key = "#limit", unless = "#result == null || #result.size() == 0")
    public List<ShortUserProfileDTO> getTopArtists(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<UserProfileModel> topArtists = userProfileRepository.findTopArtists(pageable);
        return topArtists.stream()
                .map(userProfile -> getShortUserProfileDTO(userProfile, imageService))
                .collect(Collectors.toList());
    }



    @NotNull
    public static ShortUserProfileDTO getShortUserProfileDTO(UserProfileModel userProfile, ImageService imageService) {
        ImageUrlsDTO avatar = imageService.getAvatarForUser(userProfile.getUserId().toString());

        return UserProfileModel.toShortDTO(userProfile, avatar);
    }

    @NotNull
    public static UserProfileDTO getUserProfileDTO(UserProfileModel userProfile, ImageService imageService, Short countryId) {
        ImageUrlsDTO avatar = imageService.getAvatarForUser(userProfile.getUserId().toString());

        return UserProfileModel.toDTO(userProfile, avatar, countryId);
    }

    @NotNull
    public static PublicUserProfileDTO getPublicUserProfileDTO(UserProfileModel userProfile, ImageService imageService, String countryName) {
        ImageUrlsDTO avatar = imageService.getAvatarForUser(userProfile.getUserId().toString());

        return UserProfileModel.toPublicDTO(userProfile, avatar, countryName);
    }
}
