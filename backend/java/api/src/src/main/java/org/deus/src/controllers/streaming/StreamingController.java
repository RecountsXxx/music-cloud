package org.deus.src.controllers.streaming;

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

@RestController
@RequestMapping("/api/java/protected/stream")
@RequiredArgsConstructor
public class StreamingController {
    private final StreamingService streamingService;

    @GetMapping("/playlist/{audioId}/{quality}")
    public ResponseEntity<InputStreamResource> streamPlaylist(
            @PathVariable String audioId,
            @PathVariable AudioQuality quality) {
        try {
            InputStream playlistStream = streamingService.streamPlaylist(audioId, quality);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-mpegURL")
                    .body(new InputStreamResource(playlistStream));
        } catch (DataIsNotPresentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/segment/{audioId}/{quality}/{segmentName}")
    public ResponseEntity<InputStreamResource> streamSegment(
            @PathVariable String audioId,
            @PathVariable AudioQuality quality,
            @PathVariable String segmentName) {
        try {
            InputStream segmentStream = streamingService.streamSegment(audioId, segmentName, quality);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .body(new InputStreamResource(segmentStream));
        } catch (DataIsNotPresentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
