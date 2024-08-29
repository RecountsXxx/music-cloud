package org.deus.src.controllers;

import lombok.RequiredArgsConstructor;
import org.deus.src.enums.AudioQuality;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.services.streaming.StreamingService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

/**
 * Controller for handling audio streaming requests.
 * <p><p><p>
 * This controller provides endpoints for streaming HLS playlists and segments
 * based on audio ID, audio quality, and segment name.
 */
@RestController
@RequestMapping("/api/stream/protected")
@RequiredArgsConstructor
public class StreamingController {
    private final StreamingService streamingService; // Service for streaming audio

    /**
     * Streams an HLS playlist for a given audio ID and quality.
     *
     * @param audioId The ID of the audio file.
     * @param quality The desired audio quality.
     * @return A ResponseEntity containing the playlist data as an InputStreamResource.
     */
    @GetMapping("/playlist/{audioId}/{quality}")
    public ResponseEntity<InputStreamResource> streamPlaylist(
            @PathVariable String audioId,
            @PathVariable AudioQuality quality) {
        try {
            InputStream playlistStream = streamingService.streamPlaylist(audioId, quality);

            // Set content type header for HLS playlists
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/x-mpegURL"));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(playlistStream));
        } catch (DataIsNotPresentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Streams an HLS segment for a given audio ID, segment name, and quality.
     *
     * @param audioId The ID of the audio file.
     * @param quality The desired audio quality.
     * @param segmentName The name of the HLS segment.
     * @return A ResponseEntity containing the segment data as an InputStreamResource.
     */
    @GetMapping("/segment/{audioId}/{quality}/{segmentName}")
    public ResponseEntity<InputStreamResource> streamSegment(
            @PathVariable String audioId,
            @PathVariable AudioQuality quality,
            @PathVariable String segmentName) {
        try {
            InputStream segmentStream = streamingService.streamSegment(audioId, segmentName, quality);

            // Set content type header for generic binary data
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(segmentStream));
        } catch (DataIsNotPresentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}