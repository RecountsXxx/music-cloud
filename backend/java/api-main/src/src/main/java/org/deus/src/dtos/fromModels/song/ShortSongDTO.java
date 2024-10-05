package org.deus.src.dtos.fromModels.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortSongDTO {
    private String id;
    private String name;
    private ShortReleaseDTO release;
    private Short placeNumber;
    private Float duration;
    private Integer numberOfPlays;
}
