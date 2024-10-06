package org.deus.src.dtos.fromModels.release;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortReleaseDTO {
    private String id;
    private ShortUserProfileDTO creatorUserProfile;
    private String name;
    private ImageUrlsDTO cover;
}