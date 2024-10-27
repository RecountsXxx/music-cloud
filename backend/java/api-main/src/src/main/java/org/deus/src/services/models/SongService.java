package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.PageDTO;
import org.deus.src.dtos.actions.UserProfileLikedRepostedDTO;
import org.deus.src.dtos.fromModels.comment.CommentDTO;
import org.deus.src.dtos.fromModels.genre.GenreDTO;
import org.deus.src.dtos.fromModels.playlist.SongPlaylistDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.song.PublicSongDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.song.SongDTO;
import org.deus.src.dtos.fromModels.tag.TagDTO;
import org.deus.src.dtos.helpers.AudioConvertingDTO;
import org.deus.src.enums.AudioStatus;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.exceptions.message.MessageSendingException;
import org.deus.src.models.*;
import org.deus.src.repositories.*;
import org.deus.src.requests.song.SongCreateRequest;
import org.deus.src.requests.song.SongUpdateRequest;
import org.deus.src.services.ImageService;
import org.deus.src.services.RabbitMQService;
import org.deus.src.services.models.intermediateTables.PlaylistSongService;
import org.deus.src.services.models.intermediateTables.likes.UserProfileLikedSongService;
import org.deus.src.services.models.intermediateTables.reposts.UserProfileRepostedSongService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.deus.src.services.models.ReleaseService.getShortReleaseDTO;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final ReleaseRepository releaseRepository;
    private final UserProfileRepository userProfileRepository;
    private final TagRepository tagRepository;
    private final GenreRepository genreRepository;
    private final TagService tagService;
    private final CommentService commentService;
    private final PlaylistSongService playlistSongService;
    private final UserProfileLikedSongService userProfileLikedSongService;
    private final UserProfileRepostedSongService userProfileRepostedSongService;
    private final RabbitMQService rabbitMQService;
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(SongService.class);

    @Transactional(readOnly = true)
    @Cacheable(value = "songs_pageable", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public PageDTO<ShortSongDTO> getAll(Pageable pageable) {
        Page<ShortSongDTO> page = songRepository
                .findAll(pageable)
                .map(song -> {
                    ReleaseModel release = song.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortSongDTO(song, release, creatorUserProfile, imageService);
                });

        return new PageDTO<>(page);
    }

    @Cacheable(value = "song", key = "#id")
    public SongModel getById(UUID id) throws DataNotFoundException {
        return songRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "song", key = "#id")
    public SongModel getByIdAndCreatorUserProfile(UUID id, UserProfileModel creatorUserProfile) throws DataNotFoundException, ActionCannotBePerformedException {
        SongModel song = songRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        if (!song.getRelease().getCreatorUserProfile().equals(creatorUserProfile)) {
            throw new ActionCannotBePerformedException("You don't have access to this song");
        }

        return song;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "song_dto", key = "#id")
    public SongDTO getDTOById(UUID id) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        ReleaseModel release = song.getRelease();
        UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

        return getSongDTO(song, release, creatorUserProfile, imageService);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "public_song_dto", key = "#id")
    public PublicSongDTO getPublicDTOById(UUID id) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        ReleaseModel release = song.getRelease();
        UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

        return getPublicSongDTO(song, release, creatorUserProfile, imageService);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"songs_pageable", "release", "release_dto", "public_release_dto"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "song", key = "#result.id")
            }
    )
    public SongModel create(SongCreateRequest request, UUID userId) throws DataNotFoundException, MessageSendingException, ActionCannotBePerformedException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("User Profile of creator not found"));
        ReleaseModel release = releaseRepository
                .findByIdAndCreatorUserProfile(UUID.fromString(request.getReleaseId()), creatorUserProfile)
                .orElseThrow(() -> new ActionCannotBePerformedException("Release for this song not found or you don't have permission to perform operations with it"));
        Set<GenreModel> genres = getGenresFromIds(request.getGenreIds());
        Set<TagModel> tags = getTagsFromTagNames(request.getTags());

        SongModel song = new SongModel();
        song.setTempFileId(UUID.fromString(request.getTempFileId()));
        song.setName(request.getName());
        song.setRelease(release);
        song.setPlaceNumber(request.getPlaceNumber());
        song.setDuration(request.getDuration());
        song.setStatus(AudioStatus.PROCESSING);
        song.setGenres(genres);
        song.setTags(tags);

        SongModel savedSong = songRepository.save(song);

        release.setDuration(release.getDuration() + song.getDuration());
        release.setNumberOfSongs((short) (release.getNumberOfSongs() + 1));
        releaseRepository.save(release);

        AudioConvertingDTO audioConvertingDTO = new AudioConvertingDTO(userId.toString(), savedSong.getId().toString(), request.getTempFileId());

        String queueName = "convert.audio";

        try {
            rabbitMQService.sendAudioConvertingDTO(queueName, audioConvertingDTO);
        }
        catch (MessageSendingException e) {
            throw new MessageSendingException("Failed to send the SongConvertingDTO to the message queue \"" + queueName + "\".");
        }

        return savedSong;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {
                            "songs_pageable", "playlist_songs",
                            "songs_listened_history_of_user_profile",
                            "songs_liked_by_user_profile", "songs_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = {"song_dto", "public_song_dto"}, key = "#songId")
            },
            put = {
                    @CachePut(value = "song", key = "#songId")
            }
    )
    public SongModel update(SongUpdateRequest request, UUID songId, UUID userId) throws DataNotFoundException, MessageSendingException, ActionCannotBePerformedException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("User Profile of creator not found"));
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));
        ReleaseModel release = releaseRepository
                .findByIdAndCreatorUserProfile(song.getRelease().getId(), creatorUserProfile)
                .orElseThrow(() -> new ActionCannotBePerformedException("Release for this song not found or you don't have permission to perform operations with it"));

        if (request.getName() != null && !request.getName().isEmpty()) {
            song.setName(request.getName());
        }

        if (request.getPlaceNumber() != null) {
            song.setPlaceNumber(request.getPlaceNumber());
        }

        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            Set<GenreModel> genres = getGenresFromIds(request.getGenreIds());
            song.setGenres(genres);
        }

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<TagModel> tags = getTagsFromTagNames(request.getTags());
            song.setTags(tags);
        }

        if (request.getTempFileId() != null && !request.getTempFileId().isEmpty()) {
            song.setTempFileId(UUID.fromString(request.getTempFileId()));

            if (request.getDuration() != null) {
                release.setDuration(release.getDuration() - song.getDuration() + request.getDuration());
                releaseRepository.save(release);
            }

            song.setStatus(AudioStatus.PROCESSING);

            AudioConvertingDTO audioConvertingDTO = new AudioConvertingDTO(userId.toString(), songId.toString(), request.getTempFileId());

            String queueName = "convert.audio";

            try {
                rabbitMQService.sendAudioConvertingDTO(queueName, audioConvertingDTO);
            }
            catch (MessageSendingException e) {
                throw new MessageSendingException("Failed to send the SongConvertingDTO to the message queue \"" + queueName + "\".");
            }
        }

        return songRepository.save(song);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {
                            "songs_pageable", "song_genres",
                            "song_tags", "playlist_songs",
                            "songs_listened_history_of_user_profile",
                            "songs_liked_by_user_profile", "songs_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = {"song", "song_dto", "public_song_dto", "song_genres", "song_tags"}, key = "#id")
            }
    )
    public void delete(UUID id, UUID userId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("User Profile of creator not found"));
        SongModel song = songRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));
        ReleaseModel release = releaseRepository
                .findByIdAndCreatorUserProfile(song.getRelease().getId(), creatorUserProfile)
                .orElseThrow(() -> new ActionCannotBePerformedException("Release for this song not found or you don't have permission to perform operations with it"));

        songRepository.delete(song);

        release.setDuration(release.getDuration() - song.getDuration());
        release.setNumberOfSongs((short) (release.getNumberOfSongs() - 1));
        releaseRepository.save(release);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "song_genres", key = "#songId")
    public List<GenreDTO> getGenresBySongId(UUID songId) throws DataNotFoundException {
        List<GenreModel> genres = songRepository.findGenresBySongId(songId);

        if (genres.isEmpty()) {
            throw new DataNotFoundException("No genres found for song");
        }

        return genres.stream()
                .map(GenreModel::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "song_tags", key = "#songId")
    public List<TagDTO> getTagsBySongId(UUID songId) throws DataNotFoundException {
        List<TagModel> tags = songRepository.findTagsBySongId(songId);

        if (tags.isEmpty()) {
            throw new DataNotFoundException("No tags found for song");
        }

        return tags.stream()
                .map(TagModel::toDTO)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentsBySongId(UUID songId) throws DataNotFoundException {
        return commentService.getCommentsDTOBySongId(songId);
    }



    public List<SongPlaylistDTO> getPlaylistsBySongId(UUID songId) throws DataNotFoundException {
        return playlistSongService.getPlaylistsBySongId(songId);
    }
    public List<UserProfileLikedRepostedDTO> getUserProfilesThatLiked(UUID songId) throws DataNotFoundException {
        return userProfileLikedSongService.getUserProfilesThatLikedContent(songId);
    }
    public List<UserProfileLikedRepostedDTO> getUserProfilesThatReposted(UUID songId) throws DataNotFoundException {
        return userProfileRepostedSongService.getUserProfilesThatRepostedContent(songId);
    }



    @Transactional(readOnly = true)
//    @Cacheable(value = "top_songs", key = "#limit", unless = "#result == null || #result.size() == 0")
    public List<ShortSongDTO> getTopSongs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return songRepository
                .findTopSongs(pageable).stream()
                .map(song -> {
                    ReleaseModel release = song.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortSongDTO(song, release, creatorUserProfile, imageService);
                })
                .toList();
    }



    @NotNull
    public static ShortSongDTO getShortSongDTO(SongModel song, ReleaseModel release, UserProfileModel creatorUserProfile, ImageService imageService) {
        ShortReleaseDTO releaseDTO = getShortReleaseDTO(release, creatorUserProfile, imageService);

        return SongModel.toShortDTO(song, releaseDTO);
    }

    @NotNull
    public static SongDTO getSongDTO(SongModel song, ReleaseModel release, UserProfileModel creatorUserProfile, ImageService imageService) {
        ShortReleaseDTO releaseDTO = getShortReleaseDTO(release, creatorUserProfile, imageService);

        return SongModel.toDTO(song, releaseDTO);
    }

    @NotNull
    public static PublicSongDTO getPublicSongDTO(SongModel song, ReleaseModel release, UserProfileModel creatorUserProfile, ImageService imageService) {
        ShortReleaseDTO releaseDTO = getShortReleaseDTO(release, creatorUserProfile, imageService);

        return SongModel.toPublicDTO(song, releaseDTO);
    }



    private Set<GenreModel> getGenresFromIds(Set<Short> genreIds) {
        Set<GenreModel> genres = new HashSet<>();
        if(genreIds != null) {
            if(!genreIds.isEmpty()) {
                for(Short genreId : genreIds) {
                    Optional<GenreModel> genreOptional = genreRepository.findById(genreId);
                    genreOptional.ifPresent(genres::add);
                }
            }
            else {
                logger.error("genreIds is empty");
            }
        }
        else {
            logger.error("genreIds is null");
        }

        return genres;
    }

    private Set<TagModel> getTagsFromTagNames(Set<String> tagNames) {
        Set<TagModel> tags = new HashSet<>();
        if(tagNames != null) {
            if (!tagNames.isEmpty()) {
                for(String tagName : tagNames) {
                    Optional<TagModel> existingTag = tagRepository.findByName(tagName);

                    if (existingTag.isPresent()) {
                        tags.add(existingTag.get());
                    } else {
                        tags.add(tagService.create(tagName));
                    }
                }
            }
            else {
                logger.error("tagNames is empty");
            }
        }
        else {
            logger.error("tagNames is null");
        }

        return tags;
    }
}
