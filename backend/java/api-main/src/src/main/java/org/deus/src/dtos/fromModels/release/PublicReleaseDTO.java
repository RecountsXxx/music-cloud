package org.deus.src.dtos.fromModels.release;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.enums.ReleaseType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicReleaseDTO {
    private String id;
    private ShortUserProfileDTO creatorUserProfile;
    private String name;
    private Float duration;
    private Short numberOfSongs;
    private ImageUrlsDTO cover;
    private LocalDate releaseDate;
    private ReleaseType type;
    private String buyLink;
    private String recordLabel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
