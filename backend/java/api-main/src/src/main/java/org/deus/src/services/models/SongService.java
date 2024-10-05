package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.comment.CommentDTO;
import org.deus.src.dtos.fromModels.genre.GenreDTO;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.song.SongDTO;
import org.deus.src.dtos.fromModels.tag.TagDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.dtos.helpers.AudioConvertingDTO;
import org.deus.src.enums.AudioStatus;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.exceptions.message.MessageSendingException;
import org.deus.src.models.*;
import org.deus.src.repositories.*;
import org.deus.src.requests.comment.CommentCreateUpdateRequest;
import org.deus.src.requests.song.SongCreateRequest;
import org.deus.src.requests.song.SongUpdateRequest;
import org.deus.src.services.ImageService;
import org.deus.src.services.RabbitMQService;
import org.deus.src.services.models.intermediateTables.PlaylistSongService;
import org.deus.src.services.models.intermediateTables.likes.UserProfileLikedSongService;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.deus.src.services.models.ReleaseService.getShortReleaseDTO;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final ReleaseRepository releaseRepository;
    private final TagRepository tagRepository;
    private final GenreRepository genreRepository;
    private final TagService tagService;
    private final CommentService commentService;
    private final PlaylistSongService playlistSongService;
    private final UserProfileLikedSongService userProfileLikedSongService;
    private final UserProfileRepostedSongService userProfileRepostedSongService;
    private final RabbitMQService rabbitMQService;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "songs_pageable", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ShortSongDTO> getAll(Pageable pageable) {
        return songRepository
                .findAll(pageable)
                .map(song -> {
                    ReleaseModel release = song.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortSongDTO(song, release, creatorUserProfile, imageService);
                });
    }

    @Cacheable(value = "song", key = "#id")
    public SongModel getById(UUID id) throws DataNotFoundException {
        return songRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));
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

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"songs_pageable", "release", "release_dto"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "song", key = "#result.id")
            }
    )
    public SongModel create(SongCreateRequest request, String userId) throws DataNotFoundException, MessageSendingException {
        ReleaseModel release = releaseRepository
                .findById(UUID.fromString(request.getReleaseId()))
                .orElseThrow(() -> new DataNotFoundException("Release not found"));
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

        AudioConvertingDTO audioConvertingDTO = new AudioConvertingDTO(userId, savedSong.getId().toString(), request.getTempFileId());

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
                    @CacheEvict(value = {"songs_pageable", "playlist_songs", "songs_listened_history_of_user_profile", "songs_liked_by_user_profile", "songs_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = "song_dto", key = "#result.id")
            },
            put = {
                    @CachePut(value = "song", key = "#result.id")
            }
    )
    public SongModel update(SongUpdateRequest request, String userId) throws DataNotFoundException, MessageSendingException {
        SongModel song = songRepository
                .findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

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
                ReleaseModel release = song.getRelease();
                release.setDuration(release.getDuration() - song.getDuration() + request.getDuration());
                releaseRepository.save(release);
            }

            song.setStatus(AudioStatus.PROCESSING);

            AudioConvertingDTO audioConvertingDTO = new AudioConvertingDTO(userId, request.getId(), request.getTempFileId());

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
                    @CacheEvict(value = {"songs_pageable", "song_genres", "song_tags", "playlist_songs", "songs_listened_history_of_user_profile", "songs_liked_by_user_profile", "songs_reposted_by_user_profile"}, allEntries = true),
                    @CacheEvict(value = {"song", "song_dto", "song_genres", "song_tags"}, key = "#id")
            }
    )
    public void delete(UUID id) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        ReleaseModel release = song.getRelease();

        songRepository.delete(song);

        release.setDuration(release.getDuration() - song.getDuration());
        release.setNumberOfSongs((short) (release.getNumberOfSongs() - 1));
        releaseRepository.save(release);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "song_genres", key = "#songId")
    public List<GenreDTO> getGenresBySongId(UUID songId) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        Set<GenreModel> genres = song.getGenres();

        return genres.stream()
                .map(GenreModel::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "song_tags", key = "#songId")
    public List<TagDTO> getTagsBySongId(UUID songId) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        Set<TagModel> tags = song.getTags();

        return tags.stream()
                .map(TagModel::toDTO)
                .collect(Collectors.toList());
    }



    public CommentModel addComment(CommentCreateUpdateRequest request) throws DataNotFoundException {
        return commentService.addCommentToSong(request);
    }
    public CommentModel updateComment(CommentCreateUpdateRequest request) throws DataNotFoundException {
        return commentService.updateCommentFromSong(request);
    }
    public void removeComment(UUID creatorUserProfileId, UUID songId) throws DataNotFoundException {
        commentService.removeCommentFromSong(creatorUserProfileId, songId);
    }
    public List<CommentDTO> getCommentsBySongId(UUID songId) throws DataNotFoundException {
        return commentService.getCommentsDTOBySongId(songId);
    }



    public List<ShortPlaylistDTO> getPlaylistsBySongId(UUID songId) throws DataNotFoundException {
        return playlistSongService.getPlaylistsBySongId(songId);
    }
    public List<ShortUserProfileDTO> getUserProfilesThatLiked(UUID songId) throws DataNotFoundException {
        return userProfileLikedSongService.getUserProfilesThatLikedContent(songId);
    }
    public List<ShortUserProfileDTO> getUserProfilesThatReposted(UUID songId) throws DataNotFoundException {
        return userProfileRepostedSongService.getUserProfilesThatRepostedContent(songId);
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



    private Set<GenreModel> getGenresFromIds(Set<Short> genreIds) {
        Set<GenreModel> genres = new HashSet<>();
        if(genreIds != null) {
            for(Short genreId : genreIds) {
                Optional<GenreModel> genreOptional = genreRepository.findById(genreId);
                genreOptional.ifPresent(genres::add);
            }
        }

        return genres;
    }

    private Set<TagModel> getTagsFromTagNames(Set<String> tagNames) {
        Set<TagModel> tags = new HashSet<>();
        if(tagNames != null) {
            for(String tagName : tagNames) {
                Optional<TagModel> existingTag = tagRepository.findByName(tagName);

                if (existingTag.isPresent()) {
                    tags.add(existingTag.get());
                } else {
                    tags.add(tagService.create(tagName));
                }
            }
        }

        return tags;
    }
}
