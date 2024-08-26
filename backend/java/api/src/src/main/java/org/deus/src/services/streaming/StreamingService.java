package org.deus.src.services.streaming;

import lombok.RequiredArgsConstructor;
import org.deus.src.enums.AudioQuality;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.services.storage.StorageAudioService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class StreamingService {
    private final StorageAudioService storageAudioService;

    public InputStream streamPlaylist(String audioId, AudioQuality quality) throws DataIsNotPresentException {
        return storageAudioService.getPlaylistAsStream(audioId, quality);
    }

    public InputStream streamSegment(String audioId, String segmentName, AudioQuality quality) throws DataIsNotPresentException {
        return storageAudioService.getSegmentAsStream(audioId, segmentName, quality);
    }
}
