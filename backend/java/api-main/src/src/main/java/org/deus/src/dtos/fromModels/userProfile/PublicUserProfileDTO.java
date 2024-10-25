package org.deus.src.dtos.fromModels.userProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserProfileDTO {
    private String id;
    private String username;
    private String displayName;
    private ImageUrlsDTO avatar;
    private String biography;
    private String countryName;
    private Integer numberOfFollowers;
    private Integer numberOfFollowings;
}
