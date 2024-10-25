package org.deus.src.controllers.recommendations;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.services.models.PlaylistService;
import org.deus.src.services.models.ReleaseService;
import org.deus.src.services.models.SongService;
import org.deus.src.services.models.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/general-recommendations")
@RequiredArgsConstructor
public class GeneralRecommendationsController {
    private final ReleaseService releaseService;
    private final PlaylistService playlistService;
    private final SongService songService;
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(GeneralRecommendationsController.class);

    private final int MAX_LIMIT = 50;

    @GetMapping("/top-songs")
    public @ResponseBody ResponseEntity<List<ShortSongDTO>> getTopSongs(@RequestParam(defaultValue = "5") int limit) throws StatusException {
        try {
            int effectiveLimit = getEffectiveLimit(limit);

            List<ShortSongDTO> songs = songService.getTopSongs(effectiveLimit);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(songs);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get top songs";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-releases")
    public @ResponseBody ResponseEntity<List<ShortReleaseDTO>> getTopReleases(@RequestParam(defaultValue = "5") int limit) throws StatusException {
        try {
            int effectiveLimit = getEffectiveLimit(limit);

            List<ShortReleaseDTO> releases = releaseService.getTopReleases(effectiveLimit);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(releases);
        } catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get top releases";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-playlists")
    public @ResponseBody ResponseEntity<List<ShortPlaylistDTO>> getTopPlaylists(@RequestParam(defaultValue = "5") int limit) throws StatusException {
        try {
            int effectiveLimit = getEffectiveLimit(limit);

            List<ShortPlaylistDTO> playlists = playlistService.getTopPlaylists(effectiveLimit);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(playlists);
        } catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get top playlists";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-artists")
    public @ResponseBody ResponseEntity<List<ShortUserProfileDTO>> getTopArtists(@RequestParam(defaultValue = "5") int limit) throws StatusException {
        try {
            int effectiveLimit = getEffectiveLimit(limit);

            List<ShortUserProfileDTO> artists = userProfileService.getTopArtists(effectiveLimit);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(artists);
        } catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get top artists";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public int getEffectiveLimit(int limit) {
        return Math.min(limit, MAX_LIMIT);
    }
}
