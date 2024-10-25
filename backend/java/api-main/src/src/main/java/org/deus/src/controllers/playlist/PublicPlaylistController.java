package org.deus.src.controllers.playlist;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.actions.UserProfileLikedRepostedDTO;
import org.deus.src.dtos.fromModels.playlist.PlaylistSongDTO;
import org.deus.src.dtos.fromModels.playlist.PublicPlaylistDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.responses.api.playlist.SuccessPublicPlaylistResponse;
import org.deus.src.services.models.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/playlist")
@RequiredArgsConstructor
public class PublicPlaylistController {
    private final PlaylistService playlistService;
    private static final Logger logger = LoggerFactory.getLogger(PublicPlaylistController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessPublicPlaylistResponse> getById(@PathVariable UUID id) throws StatusException {
        try {
            PublicPlaylistDTO publicPlaylistDTO = playlistService.getPublicDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessPublicPlaylistResponse(publicPlaylistDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get public playlist";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/songs")
    public @ResponseBody ResponseEntity<List<PlaylistSongDTO>> getSongs(@PathVariable UUID id) throws StatusException {
        try {
            List<PlaylistSongDTO> playlistSongDTOList = playlistService.getSongsByPlaylistId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(playlistSongDTOList);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get songs of playlist";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
