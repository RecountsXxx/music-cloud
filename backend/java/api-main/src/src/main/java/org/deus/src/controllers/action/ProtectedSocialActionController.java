package org.deus.src.controllers.action;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.enums.ActionType;
import org.deus.src.enums.ContentType;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.UserProfileModel;
import org.deus.src.responses.api.SuccessMessageResponse;
import org.deus.src.services.models.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/protected/social-action")
@RequiredArgsConstructor
public class ProtectedSocialActionController {
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(ProtectedSocialActionController.class);

    @PostMapping("/like/{actionType}/{contentType}/{contentId}")
    public @ResponseBody ResponseEntity<SuccessMessageResponse> likeAction(
            @PathVariable ActionType actionType,
            @PathVariable ContentType contentType,
            @PathVariable UUID contentId,
            @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID userProfileId = getUserProfileIdByUserId(userDTO.getId());

            switch (actionType) {
                case ADD -> userProfileService.likeContent(userProfileId, contentId, contentType);
                case REMOVE -> userProfileService.removeLikeFromContent(userProfileId, contentId, contentType);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessMessageResponse("Action with \"like\" was successfully performed"));
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to perform action with \"like\"";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/repost/{actionType}/{contentType}/{contentId}")
    public @ResponseBody ResponseEntity<SuccessMessageResponse> repostAction(
            @PathVariable ActionType actionType,
            @PathVariable ContentType contentType,
            @PathVariable UUID contentId,
            @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            UUID userProfileId = getUserProfileIdByUserId(userDTO.getId());

            switch (actionType) {
                case ADD -> userProfileService.repostContent(userProfileId, contentId, contentType);
                case REMOVE -> userProfileService.removeRepostFromContent(userProfileId, contentId, contentType);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessMessageResponse("Action with \"repost\" was successfully performed"));
        } catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to perform action with \"repost\"";
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
