package org.deus.src.dtos.fromModels.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSongDTO {
    private ShortSongDTO song;
    private LocalDateTime addedAt;
}
