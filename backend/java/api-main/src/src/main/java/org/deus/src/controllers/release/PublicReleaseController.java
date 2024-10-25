package org.deus.src.controllers.release;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.actions.UserProfileLikedRepostedDTO;
import org.deus.src.dtos.fromModels.release.PublicReleaseDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.responses.api.release.SuccessPublicReleaseResponse;
import org.deus.src.services.models.ReleaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/release")
@RequiredArgsConstructor
public class PublicReleaseController {
    private final ReleaseService releaseService;
    private static final Logger logger = LoggerFactory.getLogger(PublicReleaseController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessPublicReleaseResponse> getById(@PathVariable UUID id) throws StatusException {
        try {
            PublicReleaseDTO publicReleaseDTO = releaseService.getPublicDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessPublicReleaseResponse(publicReleaseDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get public release";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/songs")
    public @ResponseBody ResponseEntity<List<ShortSongDTO>> getSongs(@PathVariable UUID id) throws StatusException {
        try {
            List<ShortSongDTO> songs = releaseService.getSongsByReleaseId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(songs);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get songs of release";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
