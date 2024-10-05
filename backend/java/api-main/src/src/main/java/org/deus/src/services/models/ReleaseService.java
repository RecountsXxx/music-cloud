package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.release.ReleaseDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.*;
import org.deus.src.repositories.ReleaseRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.requests.release.ReleaseCreateRequest;
import org.deus.src.requests.release.ReleaseUpdateRequest;
import org.deus.src.services.ImageService;
import org.deus.src.services.models.intermediateTables.likes.UserProfileLikedReleaseService;
import org.deus.src.services.models.intermediateTables.reposts.UserProfileRepostedReleaseService;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.SongService.getShortSongDTO;
import static org.deus.src.services.models.UserProfileService.getShortUserProfileDTO;

@Service
@RequiredArgsConstructor
public class ReleaseService {
    private final ReleaseRepository releaseRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileLikedReleaseService userProfileLikedReleaseService;
    private final UserProfileRepostedReleaseService userProfileRepostedReleaseService;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "releases_pageable", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ShortReleaseDTO> getAll(Pageable pageable) {
        return releaseRepository
                .findAll(pageable)
                .map(release -> {
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortReleaseDTO(release, creatorUserProfile, imageService);
                });
    }

    @Cacheable(value = "release", key = "#id")
    public ReleaseModel getById(UUID id) throws DataNotFoundException {
        return releaseRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "release_dto", key = "#id")
    public ReleaseDTO getDTOById(UUID id) throws DataNotFoundException {
        ReleaseModel release = releaseRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));
        UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

        return getReleaseDTO(release, creatorUserProfile, imageService);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"releases_pageable", "releases_by_creator_user_profile"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "release", key = "#result.id")
            }
    )
    public ReleaseModel create(ReleaseCreateRequest request) throws DataNotFoundException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findById(UUID.fromString(request.getCreatorUserProfileId()))
                .orElseThrow(() -> new DataNotFoundException("User Profile of creator not found"));

        ReleaseModel release = createReleaseModel(request, creatorUserProfile);

        ReleaseModel savedRelease = releaseRepository.save(release);

        creatorUserProfile.setNumberOfReleases((short) (creatorUserProfile.getNumberOfReleases() + 1));
        userProfileRepository.save(creatorUserProfile);

        return savedRelease;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"releases_pageable", "release_songs", "releases_by_creator_user_profile", "releases_liked_by_user_profile", "releases_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = "release_dto", key = "#result.id")
            },
            put = {
                    @CachePut(value = "release", key = "#result.id")
            }
    )
    public ReleaseModel update(ReleaseUpdateRequest request) throws DataNotFoundException {
        ReleaseModel release = releaseRepository
                .findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        if (request.getName() != null) {
            release.setName(request.getName());
        }
        if (request.getPrivacy() != null) {
            release.setPrivacy(request.getPrivacy());
        }
        if (request.getReleaseDate() != null) {
            release.setReleaseDate(request.getReleaseDate());
        }
        if (request.getType() != null) {
            release.setType(request.getType());
        }
        if (request.getBuyLink() != null) {
            release.setBuyLink(request.getBuyLink());
        }
        if (request.getRecordLabel() != null) {
            release.setRecordLabel(request.getRecordLabel());
        }

        return releaseRepository.save(release);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"releases_pageable", "release_songs", "releases_by_creator_user_profile", "releases_liked_by_user_profile", "releases_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = {"release", "release_dto"}, key = "#id")
            }
    )
    public void delete(UUID id) throws DataNotFoundException {
        ReleaseModel release = releaseRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

        releaseRepository.delete(release);

        creatorUserProfile.setNumberOfReleases((short) (creatorUserProfile.getNumberOfReleases() - 1));
        userProfileRepository.save(creatorUserProfile);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "release_songs", key = "#releaseId")
    public List<ShortSongDTO> getSongsByReleaseId(UUID releaseId) throws DataNotFoundException {
        ReleaseModel release = releaseRepository
                .findById(releaseId)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        Set<SongModel> songs = release.getSongs();

        return songs.stream()
                .map(song -> {
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortSongDTO(song, release, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    public List<ShortUserProfileDTO> getUserProfilesThatLiked(UUID releaseId) throws DataNotFoundException {
        return userProfileLikedReleaseService.getUserProfilesThatLikedContent(releaseId);
    }

    public List<ShortUserProfileDTO> getUserProfilesThatReposted(UUID releaseId) throws DataNotFoundException {
        return userProfileRepostedReleaseService.getUserProfilesThatRepostedContent(releaseId);
    }



    @NotNull
    public static ShortReleaseDTO getShortReleaseDTO(ReleaseModel release, UserProfileModel creatorUserProfile, ImageService imageService) {
        ShortUserProfileDTO creatorUserProfileDTO = getShortUserProfileDTO(creatorUserProfile, imageService);

        ImageUrlsDTO cover = imageService.getCoverForCollection(release.getId().toString());

        return ReleaseModel.toShortDTO(release, creatorUserProfileDTO, cover);
    }

    @NotNull
    public static ReleaseDTO getReleaseDTO(ReleaseModel release, UserProfileModel creatorUserProfile, ImageService imageService) {
        ShortUserProfileDTO creatorUserProfileDTO = getShortUserProfileDTO(creatorUserProfile, imageService);

        ImageUrlsDTO cover = imageService.getCoverForCollection(release.getId().toString());

        return ReleaseModel.toDTO(release, creatorUserProfileDTO, cover);
    }

    @NotNull
    private static ReleaseModel createReleaseModel(ReleaseCreateRequest request, UserProfileModel creatorUserProfile) {
        ReleaseModel release = new ReleaseModel();

        release.setName(request.getName());
        release.setPrivacy(request.getPrivacy());
        release.setCreatorUserProfile(creatorUserProfile);
        release.setReleaseDate(request.getReleaseDate());
        release.setType(request.getType());
        release.setBuyLink(request.getBuyLink());
        release.setRecordLabel(request.getRecordLabel());
        return release;
    }
}
