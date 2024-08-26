package org.deus.src.services.storage;

import lombok.AllArgsConstructor;
import org.deus.src.drivers.StorageDriverInterface;
import org.deus.src.enums.AudioQuality;
import org.deus.src.exceptions.StorageException;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.exceptions.data.DataSavingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Service for audio storage operations.
 */
@Service
@AllArgsConstructor
public class StorageAudioService {
    // Interface for interacting with the storage driver
    private final StorageDriverInterface storage;

    // Logger for recording messages
    private static final Logger logger = LoggerFactory.getLogger(StorageAudioService.class);

    // Name of the bucket used for audio files
    private final String bucketName = "audios";

    /**
     * Builds a path for storing an audio file based on audio ID, audio quality, and object type.
     *
     * @param audioId The ID of the audio file.
     * @param quality The audio quality (e.g., LOW, MEDIUM, HIGH).
     * @param object The type of object (e.g., "playlist.m3u8" for playlist, segment name for HLS segments).
     * @return The path to the file in the storage system.
     */
    private String buildPath(String audioId, AudioQuality quality, String object) {
        return audioId + "/audio-quality-" + quality.toString() + "-" + object;
    }

    /**
     * Stores an HLS segment in temporary storage.
     *
     * @param audioId The ID of the audio file.
     * @param segmentBytes The byte array containing the HLS segment data.
     * @param segmentName The name of the HLS segment.
     * @param quality The audio quality of the segment.
     * @throws DataSavingException If an error occurs while saving the segment.
     */
    public void putHlsSegment(String audioId, byte[] segmentBytes, String segmentName, AudioQuality quality) throws DataSavingException {
        try {
            String segmentPath = buildPath(audioId, quality, segmentName);
            this.storage.putObject(bucketName, segmentPath, segmentBytes);
        } catch (StorageException e) {
            String errorMessage = "Error while saving HLS segment: " + segmentName;
            logger.error(errorMessage, e);
            throw new DataSavingException(errorMessage, e);
        }
    }

    /**
     * Stores an HLS playlist in temporary storage.
     *
     * @param audioId The ID of the audio file.
     * @param playlistBytes The byte array containing the HLS playlist data.
     * @param quality The audio quality of the playlist.
     * @throws DataSavingException If an error occurs while saving the playlist.
     */
    public void putHlsPlaylist(String audioId, byte[] playlistBytes, AudioQuality quality) throws DataSavingException {
        try {
            String playlistPath = buildPath(audioId, quality, "playlist.m3u8");
            this.storage.putObject(bucketName, playlistPath, playlistBytes);
        } catch (StorageException e) {
            String errorMessage = "Error while saving HLS playlist";
            logger.error(errorMessage, e);
            throw new DataSavingException(errorMessage, e);
        }
    }

    /**
     * Retrieves an HLS playlist as an InputStream.
     *
     * @param audioId The ID of the audio file.
     * @param quality The audio quality of the playlist.
     * @return An InputStream containing the playlist data, or throws an exception if not found.
     * @throws DataIsNotPresentException If the playlist is not found in storage.
     */
    public InputStream getPlaylistAsStream(String audioId, AudioQuality quality) throws DataIsNotPresentException {
        try {
            String playlistPath = buildPath(audioId, quality, "playlist.m3u8");
            return storage.getObjectAsStream(bucketName, playlistPath);
        } catch (StorageException e) {
            String errorMessage = "Playlist for audio with id \"" + audioId + "\" not found";
            logger.error(errorMessage, e);
            throw new DataIsNotPresentException(errorMessage, e);
        }
    }

    /**
     * Retrieves an HLS segment as an InputStream.
     *
     * @param audioId The ID of the audio file.
     * @param segmentName The name of the HLS segment.
     * @param quality The audio quality of the segment.
     * @return An InputStream containing the segment data, or throws an exception if not found.
     * @throws DataIsNotPresentException If the segment is not found in storage.
     */
    public InputStream getSegmentAsStream(String audioId, String segmentName, AudioQuality quality) throws DataIsNotPresentException {
        try {
            String segmentPath = buildPath(audioId, quality, segmentName);
            return storage.getObjectAsStream(bucketName, segmentPath);
        } catch (StorageException e) {
            String errorMessage = "Segment \"" + segmentName + "\" for audio with id \"" + audioId + "\" not found";
            logger.error(errorMessage, e);
            throw new DataIsNotPresentException(errorMessage, e);
        }
    }
}
