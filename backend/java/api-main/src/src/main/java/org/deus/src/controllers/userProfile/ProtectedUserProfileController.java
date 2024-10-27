package org.deus.src.controllers.userProfile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.PageDTO;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.fromModels.song.SongListenedDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileActionDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileDTO;
import org.deus.src.enums.*;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.UserProfileModel;
import org.deus.src.requests.userProfile.UserProfileCreateRequest;
import org.deus.src.requests.userProfile.UserProfileUpdateRequest;
import org.deus.src.responses.api.SuccessMessageResponse;
import org.deus.src.responses.api.userProfile.SuccessUserProfileResponse;
import org.deus.src.services.models.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/user-profile")
@RequiredArgsConstructor
public class ProtectedUserProfileController {
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(ProtectedUserProfileController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessUserProfileResponse> getById(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            if(!userProfileService.existsByIdAndUserId(id, UUID.fromString(userDTO.getId()))) {
                throw new ActionCannotBePerformedException("User profile not found or you don't have access to it");
            }

            UserProfileDTO userProfileDTO = userProfileService.getDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessUserProfileResponse(userProfileDTO));
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get user profile";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<PageDTO<ShortUserProfileDTO>> getAll(Pageable pageable) throws StatusException {
        try {
            PageDTO<ShortUserProfileDTO> userProfilesPage = userProfileService.getAll(pageable);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userProfilesPage);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get page of user profiles";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessUserProfileResponse> create(@RequestBody @Valid UserProfileCreateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UserProfileModel userProfile = userProfileService.create(request, UUID.fromString(userDTO.getId()));
            UserProfileDTO userProfileDTO = userProfileService.getDTOById(userProfile.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessUserProfileResponse(userProfileDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to create user profile";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessUserProfileResponse> update(@PathVariable UUID id, @RequestBody @Valid UserProfileUpdateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UserProfileModel userProfile = userProfileService.update(request, id, UUID.fromString(userDTO.getId()));
            UserProfileDTO userProfileDTO = userProfileService.getDTOById(userProfile.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessUserProfileResponse(userProfileDTO));
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
            String errorMessage = "Something went wrong while trying to update user profile";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            userProfileService.delete(id, UUID.fromString(userDTO.getId()));

            return ResponseEntity.noContent().build();
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to delete user profile";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/follow/{followingId}/{actionType}")
    public @ResponseBody ResponseEntity<SuccessMessageResponse> followAction(
            @PathVariable UUID followingId,
            @PathVariable FollowActionType actionType,
            @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID followerId = getUserProfileIdByUserId(userDTO.getId());

            switch (actionType) {
                case FOLLOW -> userProfileService.followUser(followerId, followingId);
                case UNFOLLOW -> userProfileService.unfollowUser(followerId, followingId);
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessMessageResponse("Action with \"follow\" was successfully performed"));
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to perform action with \"follow\"";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/block/{blockedId}/{actionType}")
    public @ResponseBody ResponseEntity<SuccessMessageResponse> blockAction(
            @PathVariable UUID blockedId,
            @PathVariable BlockActionType actionType,
            @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID blockerId = getUserProfileIdByUserId(userDTO.getId());

            switch (actionType) {
                case BLOCK -> userProfileService.blockUser(blockerId, blockedId);
                case UNBLOCK -> userProfileService.unblockUser(blockerId, blockedId);
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessMessageResponse("Action with \"block\" was successfully performed"));
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to perform action with \"block\"";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/blocked-users")
    public @ResponseBody ResponseEntity<List<UserProfileActionDTO>> getBlockedUsers(@RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID userProfileId = getUserProfileIdByUserId(userDTO.getId());

            List<UserProfileActionDTO> blockedUsers = userProfileService.getBlockedUsers(userProfileId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(blockedUsers);
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get blocked users";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/listened-history/{songId}")
    public @ResponseBody ResponseEntity<SuccessMessageResponse> addSongToListenedHistory(@PathVariable UUID songId, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID userProfileId = getUserProfileIdByUserId(userDTO.getId());

            userProfileService.addSongToHistory(userProfileId, songId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessMessageResponse("Song was successfully added to the listened history"));
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to add song to the listened history";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listened-history")
    public @ResponseBody ResponseEntity<List<SongListenedDTO>> getListenedHistory(@RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID userProfileId = getUserProfileIdByUserId(userDTO.getId());

            List<SongListenedDTO> listenedHistory = userProfileService.getSongsListenedHistory(userProfileId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(listenedHistory);
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get listened history";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/listened-history")
    public ResponseEntity<Void> clearListenedHistory(@RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID userProfileId = getUserProfileIdByUserId(userDTO.getId());

            userProfileService.clearHistory(userProfileId);

            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to clear listened history";
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
