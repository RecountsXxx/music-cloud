package org.deus.src.dtos.fromModels.release;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.baseDTO.CollectionDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileDTO;
import org.deus.src.enums.Privacy;
import org.deus.src.enums.ReleaseType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseDTO extends CollectionDTO {
    private LocalDate releaseDate;
    private ReleaseType type;
    private String buyLink;
    private String recordLabel;

    public ReleaseDTO (String id, String name, Float duration, Privacy privacy, Short numberOfSongs, ImageUrlsDTO cover,  LocalDateTime createdAt, LocalDateTime updatedAt, ShortUserProfileDTO creatorUserProfile, LocalDate releaseDate, ReleaseType type, String buyLink, String recordLabel) {
        super(id, creatorUserProfile, name, duration, privacy, numberOfSongs, cover, createdAt, updatedAt);
        this.releaseDate = releaseDate;
        this.type = type;
        this.buyLink = buyLink;
        this.recordLabel = recordLabel;
    }
}
