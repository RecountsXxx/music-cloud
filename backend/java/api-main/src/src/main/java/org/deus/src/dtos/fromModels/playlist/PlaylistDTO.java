package org.deus.src.dtos.fromModels.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.baseDTO.CollectionDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileDTO;
import org.deus.src.enums.Privacy;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDTO extends CollectionDTO {
    private String description;

    public PlaylistDTO (String id, String name, Float duration, Privacy privacy, Short numberOfSongs, ImageUrlsDTO cover, LocalDateTime createdAt, LocalDateTime updatedAt, ShortUserProfileDTO creatorUserProfile, String description) {
        super(id, creatorUserProfile, name, duration, privacy, numberOfSongs, cover, createdAt, updatedAt);
        this.description = description;
    }
}
