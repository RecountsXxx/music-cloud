package org.deus.src.controllers.release;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.PageDTO;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.fromModels.release.ReleaseDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.requests.release.ReleaseCreateRequest;
import org.deus.src.requests.release.ReleaseUpdateRequest;
import org.deus.src.responses.api.release.SuccessReleaseResponse;
import org.deus.src.services.models.ReleaseService;
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
@RequestMapping("/api/protected/release")
@RequiredArgsConstructor
public class ProtectedReleaseController {
    private final ReleaseService releaseService;
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(ProtectedReleaseController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessReleaseResponse> getById(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UserProfileModel userProfile = getUserProfileByUserId(userDTO.getId());
            releaseService.getByIdAndCreatorUserProfile(id, userProfile);

            ReleaseDTO releaseDTO = releaseService.getDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessReleaseResponse(releaseDTO));
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
            String errorMessage = "Something went wrong while trying to get release";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<PageDTO<ShortReleaseDTO>> getAll(Pageable pageable) throws StatusException {
        try {
            PageDTO<ShortReleaseDTO> shortReleasesPage = releaseService.getAll(pageable);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(shortReleasesPage);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get page of releases";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessReleaseResponse> create(@RequestBody @Valid ReleaseCreateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            ReleaseModel release = releaseService.create(request, UUID.fromString(userDTO.getId()));
            ReleaseDTO releaseDTO = releaseService.getDTOById(release.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessReleaseResponse(releaseDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to create release";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessReleaseResponse> update(@PathVariable UUID id, @RequestBody @Valid ReleaseUpdateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            ReleaseModel release = releaseService.update(request, id, UUID.fromString(userDTO.getId()));
            ReleaseDTO releaseDTO = releaseService.getDTOById(release.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessReleaseResponse(releaseDTO));
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
            String errorMessage = "Something went wrong while trying to update release";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            releaseService.delete(id, UUID.fromString(userDTO.getId()));

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
            String errorMessage = "Something went wrong while trying to delete release";
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
