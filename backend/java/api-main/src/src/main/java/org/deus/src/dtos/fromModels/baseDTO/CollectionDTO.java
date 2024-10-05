package org.deus.src.dtos.fromModels.baseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.enums.Privacy;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDTO {
    private String id;
    private ShortUserProfileDTO creatorUserProfile;
    private String name;
    private Float duration;
    private Privacy privacy;
    private Short numberOfSongs;
    private ImageUrlsDTO cover;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
