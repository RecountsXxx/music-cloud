package org.deus.src.dtos.fromModels.userProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.enums.AudioQuality;
import org.deus.src.enums.Gender;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String id;
    private String username;
    private String displayName;
    private ImageUrlsDTO avatar;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Gender gender;
    private AudioQuality preferredQuality;
    private String biography;
    private Short countryId;
    private Integer numberOfFollowers;
    private Integer numberOfFollowings;
    private Integer numberOfBlockedUsers;
}
