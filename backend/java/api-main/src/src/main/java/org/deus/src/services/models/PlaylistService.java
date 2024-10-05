package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.playlist.PlaylistDTO;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.repositories.PlaylistRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.requests.playlist.PlaylistCreateRequest;
import org.deus.src.requests.playlist.PlaylistUpdateRequest;
import org.deus.src.services.ImageService;
import org.deus.src.services.models.intermediateTables.PlaylistSongService;
import org.deus.src.services.models.intermediateTables.likes.UserProfileLikedPlaylistService;
import org.deus.src.services.models.intermediateTables.reposts.UserProfileRepostedPlaylistService;
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

import static org.deus.src.services.models.UserProfileService.getShortUserProfileDTO;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserProfileRepository userProfileRepository;
    private final PlaylistSongService playlistSongService;
    private final UserProfileLikedPlaylistService userProfileLikedPlaylistService;
    private final UserProfileRepostedPlaylistService userProfileRepostedPlaylistService;
    private final ImageService imageService;

    @Cacheable(value = "playlists_pageable", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ShortPlaylistDTO> getAll(Pageable pageable) {
        return playlistRepository
                .findAll(pageable)
                .map(playlist -> {
                    UserProfileModel creatorUserProfile = playlist.getCreatorUserProfile();

                    return getShortPlaylistDTO(playlist, creatorUserProfile, imageService);
                });
    }

    @Cacheable(value = "playlist", key = "#id")
    public PlaylistModel getById(UUID id) throws DataNotFoundException {
        return playlistRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));
    }

    @Cacheable(value = "playlist_dto", key = "#id")
    public PlaylistDTO getDTOById(UUID id) throws DataNotFoundException {
        PlaylistModel playlist = playlistRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));
        UserProfileModel creatorUserProfile = playlist.getCreatorUserProfile();

        return getPlaylistDTO(playlist, creatorUserProfile, imageService);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"playlists_pageable", "playlists_by_creator_user_profile"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "playlist", key = "#result.id")
            }
    )
    public PlaylistModel create(PlaylistCreateRequest request) throws DataNotFoundException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findById(UUID.fromString(request.getCreatorUserProfileId()))
                .orElseThrow(() -> new DataNotFoundException("User Profile of creator not found"));

        PlaylistModel playlist = new PlaylistModel();
        playlist.setName(request.getName());
        playlist.setPrivacy(request.getPrivacy());
        playlist.setCreatorUserProfile(creatorUserProfile);
        if (request.getDescription() != null) {
            playlist.setDescription(request.getDescription());
        }

        PlaylistModel savedPlaylist = playlistRepository.save(playlist);

        creatorUserProfile.setNumberOfCreatedPlaylists((short) (creatorUserProfile.getNumberOfCreatedPlaylists() + 1));
        userProfileRepository.save(creatorUserProfile);

        return savedPlaylist;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"playlists_pageable", "song_playlists", "playlists_by_creator_user_profile", "playlists_liked_by_user_profile", "playlists_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = "playlist_dto", key = "#result.id")
            },
            put = {
                    @CachePut(value = "playlist", key = "#result.id")
            }
    )
    public PlaylistModel update(PlaylistUpdateRequest request) throws DataNotFoundException {
        PlaylistModel playlist = playlistRepository
                .findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        if (request.getName() != null) {
            playlist.setName(request.getName());
        }
        if (request.getPrivacy() != null) {
            playlist.setPrivacy(request.getPrivacy());
        }
        if (request.getDescription() != null) {
            playlist.setDescription(request.getDescription());
        }

        return playlistRepository.save(playlist);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"playlists_pageable", "song_playlists", "playlists_by_creator_user_profile", "playlists_liked_by_user_profile", "playlists_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = {"playlist", "playlist_dto"}, key = "#id")
            }
    )
    public void delete(UUID id) throws DataNotFoundException {
        PlaylistModel playlist = playlistRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        UserProfileModel creatorUserProfile = playlist.getCreatorUserProfile();

        playlistRepository.delete(playlist);

        creatorUserProfile.setNumberOfCreatedPlaylists((short) (creatorUserProfile.getNumberOfCreatedPlaylists() - 1));
        userProfileRepository.save(creatorUserProfile);
    }



    public void addSongToPlaylist(UUID playlistId, UUID songId) throws DataNotFoundException, ActionCannotBePerformedException {
        playlistSongService.addSongToPlaylist(playlistId, songId);
    }
    public void removeSongFromPlaylist(UUID playlistId, UUID songId) throws DataNotFoundException, ActionCannotBePerformedException {
        playlistSongService.removeSongFromPlaylist(playlistId, songId);
    }



    public List<ShortSongDTO> getSongsByPlaylistId(UUID playlistId) throws DataNotFoundException {
        return playlistSongService.getSongsByPlaylistId(playlistId);
    }
    public List<ShortUserProfileDTO> getUserProfilesThatLiked(UUID playlistId) throws DataNotFoundException {
        return userProfileLikedPlaylistService.getUserProfilesThatLikedContent(playlistId);
    }

    public List<ShortUserProfileDTO> getUserProfilesThatReposted(UUID playlistId) throws DataNotFoundException {
        return userProfileRepostedPlaylistService.getUserProfilesThatRepostedContent(playlistId);
    }



    @NotNull
    public static ShortPlaylistDTO getShortPlaylistDTO(PlaylistModel playlist, UserProfileModel creatorUserProfile, ImageService imageService) {
        ShortUserProfileDTO creatorUserProfileDTO = getShortUserProfileDTO(creatorUserProfile, imageService);

        ImageUrlsDTO cover = imageService.getCoverForCollection(playlist.getId().toString());

        return PlaylistModel.toShortDTO(playlist, creatorUserProfileDTO, cover);
    }

    @NotNull
    public static PlaylistDTO getPlaylistDTO(PlaylistModel playlist, UserProfileModel creatorUserProfile, ImageService imageService) {
        ShortUserProfileDTO creatorUserProfileDTO = getShortUserProfileDTO(creatorUserProfile, imageService);

        ImageUrlsDTO cover = imageService.getCoverForCollection(playlist.getId().toString());

        return PlaylistModel.toDTO(playlist, creatorUserProfileDTO, cover);
    }
}
