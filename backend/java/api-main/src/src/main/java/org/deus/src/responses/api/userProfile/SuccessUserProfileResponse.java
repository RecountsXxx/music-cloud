package org.deus.src.responses.api.userProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.userProfile.UserProfileDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessUserProfileResponse {
    private UserProfileDTO userProfile;
}
