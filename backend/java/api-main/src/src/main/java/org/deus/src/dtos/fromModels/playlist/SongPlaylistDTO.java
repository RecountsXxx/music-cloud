package org.deus.src.dtos.fromModels.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongPlaylistDTO {
    private ShortPlaylistDTO playlist;
    private LocalDateTime addedAt;
}
