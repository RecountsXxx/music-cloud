package org.deus.src.dtos.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileLikedRepostedDTO {
    private ShortUserProfileDTO userProfile;
    private LocalDateTime createdAt;
}
