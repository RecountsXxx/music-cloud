package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.comment.CommentDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.CommentModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.repositories.CommentRepository;
import org.deus.src.repositories.SongRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.requests.comment.CommentCreateUpdateRequest;
import org.deus.src.services.ImageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserProfileRepository userProfileRepository;
    private final SongRepository songRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "comments_by_song", key = "#songId")
    public List<CommentModel> getCommentsBySongId(UUID songId) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        return new ArrayList<>(commentRepository
                .findBySong(song));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "comments_dto_by_song", key = "#songId")
    public List<CommentDTO> getCommentsDTOBySongId(UUID songId) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        return commentRepository
                .findBySong(song).stream()
                .map(commentModel -> {
                    UserProfileModel creatorUserProfile = commentModel.getCreatorUserProfile();

                    ImageUrlsDTO avatar = imageService.getAvatarForUser(creatorUserProfile.getUserId().toString());

                    ShortUserProfileDTO creatorUserProfileDTO = UserProfileModel.toShortDTO(creatorUserProfile, avatar);

                    return CommentModel.toDTO(commentModel, creatorUserProfileDTO);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "comments_by_song", key = "#request.songId"),
            @CacheEvict(value = "comments_dto_by_song", key = "#request.songId")
    })
    public CommentModel addCommentToSong(CommentCreateUpdateRequest request) throws DataNotFoundException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findById(UUID.fromString(request.getCreatorUserProfileId()))
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(UUID.fromString(request.getSongId()))
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        CommentModel comment = new CommentModel();
        comment.setCreatorUserProfile(creatorUserProfile);
        comment.setSong(song);
        comment.setContent(request.getContent());

        CommentModel savedComment = commentRepository.save(comment);

        song.setNumberOfComments(song.getNumberOfComments() + 1);
        songRepository.save(song);

        return savedComment;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "comments_by_song", key = "#request.songId"),
            @CacheEvict(value = "comments_dto_by_song", key = "#request.songId")
    })
    public CommentModel updateCommentFromSong(CommentCreateUpdateRequest request) throws DataNotFoundException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findById(UUID.fromString(request.getCreatorUserProfileId()))
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(UUID.fromString(request.getSongId()))
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        CommentModel comment = commentRepository
                .findByCreatorUserProfileAndSong(creatorUserProfile, song)
                .orElseThrow(() -> new DataNotFoundException("Comment not found"));

        if (request.getContent() != null) {
            comment.setContent(request.getContent());
        }

        return commentRepository.save(comment);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "comments_by_song", key = "#songId"),
            @CacheEvict(value = "comments_dto_by_song", key = "#songId")
    })
    public void removeCommentFromSong(UUID creatorUserProfileId, UUID songId) throws DataNotFoundException {
        UserProfileModel creatorUserProfile = userProfileRepository
                .findById(creatorUserProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        CommentModel comment = commentRepository
                .findByCreatorUserProfileAndSong(creatorUserProfile, song)
                .orElseThrow(() -> new DataNotFoundException("Comment not found"));

        commentRepository.delete(comment);

        song.setNumberOfComments(song.getNumberOfComments() - 1);
        songRepository.save(song);
    }
}
