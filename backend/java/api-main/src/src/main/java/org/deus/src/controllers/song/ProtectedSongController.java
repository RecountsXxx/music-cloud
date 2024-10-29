package org.deus.src.controllers.song;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.PageDTO;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.song.SongDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.exceptions.message.MessageSendingException;
import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.requests.song.SongCreateRequest;
import org.deus.src.requests.song.SongUpdateRequest;
import org.deus.src.responses.api.song.SuccessSongResponse;
import org.deus.src.services.models.CommentService;
import org.deus.src.services.models.SongService;
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
@RequestMapping("/api/protected/song")
@RequiredArgsConstructor
public class ProtectedSongController {
    private final SongService songService;
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(ProtectedSongController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessSongResponse> getById(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UserProfileModel userProfile = getUserProfileByUserId(userDTO.getId());
            songService.getByIdAndCreatorUserProfile(id, userProfile);

            SongDTO songDTO = songService.getDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessSongResponse(songDTO));
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
            String errorMessage = "Something went wrong while trying to get song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<PageDTO<ShortSongDTO>> getAll(Pageable pageable) throws StatusException {
        try {
            PageDTO<ShortSongDTO> shortSongsPage = songService.getAll(pageable);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(shortSongsPage);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get page of songs";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessSongResponse> create(@RequestBody @Valid SongCreateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            SongModel song = songService.create(request, UUID.fromString(userDTO.getId()));
            SongDTO songDTO = songService.getDTOById(song.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessSongResponse(songDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (MessageSendingException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException("We couldn't prepare your song to be ready for use, try later", HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to create song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessSongResponse> update(@PathVariable UUID id, @RequestBody @Valid SongUpdateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            SongModel song = songService.update(request, id, UUID.fromString(userDTO.getId()));
            SongDTO songDTO = songService.getDTOById(song.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessSongResponse(songDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (MessageSendingException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException("We couldn't prepare your song to be ready for use, try later", HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to create song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            songService.delete(id, UUID.fromString(userDTO.getId()));

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
            String errorMessage = "Something went wrong while trying to delete song";
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
