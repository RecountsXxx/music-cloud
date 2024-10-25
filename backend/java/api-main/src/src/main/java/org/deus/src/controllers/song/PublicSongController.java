package org.deus.src.controllers.song;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.actions.UserProfileLikedRepostedDTO;
import org.deus.src.dtos.fromModels.comment.CommentDTO;
import org.deus.src.dtos.fromModels.genre.GenreDTO;
import org.deus.src.dtos.fromModels.playlist.SongPlaylistDTO;
import org.deus.src.dtos.fromModels.song.PublicSongDTO;
import org.deus.src.dtos.fromModels.tag.TagDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.responses.api.song.SuccessPublicSongResponse;
import org.deus.src.services.models.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/song")
@RequiredArgsConstructor
public class PublicSongController {
    private final SongService songService;
    private static final Logger logger = LoggerFactory.getLogger(PublicSongController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessPublicSongResponse> getById(@PathVariable UUID id) throws StatusException {
        try {
            PublicSongDTO publicSongDTO = songService.getPublicDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessPublicSongResponse(publicSongDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get public song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/genres")
    public @ResponseBody ResponseEntity<List<GenreDTO>> getGenres(@PathVariable UUID id) throws StatusException {
        try {
            List<GenreDTO> genres = songService.getGenresBySongId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(genres);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get genres of the song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/tags")
    public @ResponseBody ResponseEntity<List<TagDTO>> getTags(@PathVariable UUID id) throws StatusException {
        try {
            List<TagDTO> tags = songService.getTagsBySongId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(tags);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get tags of the song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/comments")
    public @ResponseBody ResponseEntity<List<CommentDTO>> getComments(@PathVariable UUID id) throws StatusException {
        try {
            List<CommentDTO> tags = songService.getCommentsBySongId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(tags);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get comments of the song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/in-playlists")
    public @ResponseBody ResponseEntity<List<SongPlaylistDTO>> getPlaylists(@PathVariable UUID id) throws StatusException {
        try {
            List<SongPlaylistDTO> playlists = songService.getPlaylistsBySongId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(playlists);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get playlists that contains the song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
