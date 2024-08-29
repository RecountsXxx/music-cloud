package org.deus.src.services.streaming;

import lombok.RequiredArgsConstructor;
import org.deus.src.enums.AudioQuality;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.services.storage.StorageAudioService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Service for audio streaming operations.
 */
@Service
@RequiredArgsConstructor
public class StreamingService {
    private final StorageAudioService storageAudioService; // Service for interacting with audio storage

    /**
     * Streams an HLS playlist for a given audio ID and quality.
     *
     * @param audioId The ID of the audio file.
     * @param quality The desired audio quality.
     * @return An InputStream containing the playlist data.
     * @throws DataIsNotPresentException If the playlist is not found.
     */
    public InputStream streamPlaylist(String audioId, AudioQuality quality) throws DataIsNotPresentException {
        return storageAudioService.getPlaylistAsStream(audioId, quality);
    }

    /**
     * Streams an HLS segment for a given audio ID, segment name, and quality.
     *
     * @param audioId The ID of the audio file.
     * @param segmentName The name of the HLS segment.
     * @param quality The desired audio quality.
     * @return An InputStream containing the segment data.
     * @throws DataIsNotPresentException If the segment is not found.
     */
    public InputStream streamSegment(String audioId, String segmentName, AudioQuality quality) throws DataIsNotPresentException {
        return storageAudioService.getSegmentAsStream(audioId, segmentName, quality);
    }
}
