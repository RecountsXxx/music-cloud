package org.deus.src.controllers.action;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.actions.UserProfileLikedRepostedDTO;
import org.deus.src.enums.ContentType;
import org.deus.src.enums.SocialActionType;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.services.models.PlaylistService;
import org.deus.src.services.models.ReleaseService;
import org.deus.src.services.models.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/social-action")
@RequiredArgsConstructor
public class PublicSocialActionController {
    private final ReleaseService releaseService;
    private final PlaylistService playlistService;
    private final SongService songService;
    private static final Logger logger = LoggerFactory.getLogger(PublicSocialActionController.class);

    @GetMapping("/user-profiles/{socialActionType}/{contentType}/{contentId}")
    public @ResponseBody ResponseEntity<List<UserProfileLikedRepostedDTO>> getUserProfiles(@PathVariable SocialActionType socialActionType, @PathVariable ContentType contentType, @PathVariable UUID contentId) throws StatusException {
        try {
            List<UserProfileLikedRepostedDTO> userProfiles = null;

            switch (socialActionType) {
                case LIKE -> {
                    switch (contentType) {
                        case RELEASE -> userProfiles = releaseService.getUserProfilesThatLiked(contentId);
                        case PLAYLIST -> userProfiles = playlistService.getUserProfilesThatLiked(contentId);
                        case SONG -> userProfiles = songService.getUserProfilesThatLiked(contentId);
                    }
                }
                case REPOST -> {
                    switch (contentType) {
                        case RELEASE -> userProfiles = releaseService.getUserProfilesThatReposted(contentId);
                        case PLAYLIST -> userProfiles = playlistService.getUserProfilesThatReposted(contentId);
                        case SONG -> userProfiles = songService.getUserProfilesThatReposted(contentId);
                    }
                }
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userProfiles);
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get user profiles that performed action";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
