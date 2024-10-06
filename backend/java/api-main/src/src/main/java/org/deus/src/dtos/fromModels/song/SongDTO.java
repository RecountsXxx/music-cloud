package org.deus.src.dtos.fromModels.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.enums.AudioStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private String id;
    private String name;
    private ShortReleaseDTO release;
    private Short placeNumber;
    private Float duration;
    private AudioStatus status;
    private Integer numberOfPlays;
    private Integer numberOfLikes;
    private Integer numberOfReposts;
    private Integer numberOfComments;
    private Integer numberOfPlaylistsWhichContainsSong;
}
