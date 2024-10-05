package org.deus.src.dtos.fromModels.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.models.SongModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongCreateDTO {
    private SongModel song;
    private ShortReleaseDTO shortReleaseDTO;
}
