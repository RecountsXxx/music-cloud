package org.deus.src.controllers.playlist;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.PageDTO;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.fromModels.playlist.PlaylistDTO;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.enums.ActionType;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.requests.playlist.PlaylistCreateRequest;
import org.deus.src.requests.playlist.PlaylistUpdateRequest;
import org.deus.src.responses.api.SuccessMessageResponse;
import org.deus.src.responses.api.playlist.SuccessPlaylistResponse;
import org.deus.src.services.models.PlaylistService;
import org.deus.src.services.models.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/protected/playlist")
@RequiredArgsConstructor
public class ProtectedPlaylistController {
    private final PlaylistService playlistService;
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(ProtectedPlaylistController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessPlaylistResponse> getById(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UserProfileModel userProfile = getUserProfileByUserId(userDTO.getId());
            PlaylistModel playlist = playlistService.getByIdAndCreatorUserProfile(id, userProfile);

            PlaylistDTO playlistDTO = playlistService.getDTOById(playlist.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessPlaylistResponse(playlistDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get playlist";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<PageDTO<ShortPlaylistDTO>> getAll(Pageable pageable) throws StatusException {
        try {
            PageDTO<ShortPlaylistDTO> shortPlaylistsPage = playlistService.getAll(pageable);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(shortPlaylistsPage);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get page of playlists";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessPlaylistResponse> create(@RequestBody @Valid PlaylistCreateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            PlaylistModel playlist = playlistService.create(request, UUID.fromString(userDTO.getId()));
            PlaylistDTO playlistDTO = playlistService.getDTOById(playlist.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessPlaylistResponse(playlistDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to create playlist";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessPlaylistResponse> update(@PathVariable UUID id, @RequestBody @Valid PlaylistUpdateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            PlaylistModel playlist = playlistService.update(request, id, UUID.fromString(userDTO.getId()));
            PlaylistDTO playlistDTO = playlistService.getDTOById(playlist.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessPlaylistResponse(playlistDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to update playlist";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            playlistService.delete(id, UUID.fromString(userDTO.getId()));

            return ResponseEntity.noContent().build();
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to delete playlist";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/song/{songId}/{actionType}")
    public @ResponseBody ResponseEntity<SuccessMessageResponse> actionSong(
            @PathVariable UUID id,
            @PathVariable UUID songId,
            @PathVariable ActionType actionType,
            @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UserProfileModel userProfile = getUserProfileByUserId(userDTO.getId());
            PlaylistModel playlist = playlistService.getByIdAndCreatorUserProfile(id, userProfile);

            switch (actionType) {
                case ADD -> playlistService.addSongToPlaylist(id, songId);
                case REMOVE -> playlistService.removeSongFromPlaylist(id, songId);
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessMessageResponse("Action for \"song in playlist\" was successfully performed"));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to perform action for \"song in playlist\"";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserProfileModel getUserProfileByUserId(String userId) throws DataNotFoundException {
        return userProfileService.getByUserId(UUID.fromString(userId));
    }

    private UUID getUserProfileIdByUserId(String userId) throws DataNotFoundException {
        return getUserProfileByUserId(userId).getId();
    }
}
