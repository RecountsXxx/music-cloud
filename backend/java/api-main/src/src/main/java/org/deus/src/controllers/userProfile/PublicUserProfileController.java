package org.deus.src.controllers.userProfile;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.actions.LikeContentDTO;
import org.deus.src.dtos.actions.RepostContentDTO;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.userProfile.PublicUserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileActionDTO;
import org.deus.src.enums.ContentType;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.UserProfileModel;
import org.deus.src.responses.api.userProfile.SuccessPublicUserProfileResponse;
import org.deus.src.services.models.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/user-profile")
@RequiredArgsConstructor
public class PublicUserProfileController {
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(ProtectedUserProfileController.class);

    @GetMapping("/{username}")
    public @ResponseBody ResponseEntity<SuccessPublicUserProfileResponse> getByUsername(@PathVariable String username) throws StatusException {
        try {
            UserProfileModel userProfile = userProfileService.getByUsername(username);
            PublicUserProfileDTO publicUserProfileDTO = userProfileService.getPublicDTOById(userProfile.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessPublicUserProfileResponse(publicUserProfileDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get public user profile";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/followers")
    public @ResponseBody ResponseEntity<List<UserProfileActionDTO>> getFollowers(@PathVariable UUID id) throws StatusException {
        try {
            List<UserProfileActionDTO> followers = userProfileService.getFollowers(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(followers);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get followers";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/followings")
    public @ResponseBody ResponseEntity<List<UserProfileActionDTO>> getFollowings(@PathVariable UUID id) throws StatusException {
        try {
            List<UserProfileActionDTO> followings = userProfileService.getFollowings(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(followings);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get followings";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/releases")
    public @ResponseBody ResponseEntity<List<ShortReleaseDTO>> getReleases(@PathVariable UUID id) throws StatusException {
        try {
            List<ShortReleaseDTO> releases = userProfileService.getReleasesByCreatorUserProfileId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(releases);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get releases";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/playlists")
    public @ResponseBody ResponseEntity<List<ShortPlaylistDTO>> getPlaylists(@PathVariable UUID id) throws StatusException {
        try {
            List<ShortPlaylistDTO> playlists = userProfileService.getPlaylistsByCreatorUserProfileId(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(playlists);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get playlists";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/likes/{contentType}")
    public @ResponseBody ResponseEntity<List<LikeContentDTO>> getLikes(@PathVariable UUID id, @PathVariable ContentType contentType) throws StatusException {
        try {
            List<LikeContentDTO> contentList = null;

            switch (contentType) {
                case RELEASE -> contentList = userProfileService.getLikedReleases(id);
                case SONG -> contentList = userProfileService.getLikedSongs(id);
                case PLAYLIST -> contentList = userProfileService.getLikedPlaylists(id);
            };

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(contentList);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get liked content";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/reposts/{contentType}")
    public @ResponseBody ResponseEntity<List<RepostContentDTO>> getReposts(@PathVariable UUID id, @PathVariable ContentType contentType) throws StatusException {
        try {
            List<RepostContentDTO> contentList = null;

            switch (contentType) {
                case RELEASE -> contentList = userProfileService.getRepostedReleases(id);
                case SONG -> contentList = userProfileService.getRepostedSongs(id);
                case PLAYLIST -> contentList = userProfileService.getRepostedPlaylists(id);
            };

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(contentList);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get reposted content";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
