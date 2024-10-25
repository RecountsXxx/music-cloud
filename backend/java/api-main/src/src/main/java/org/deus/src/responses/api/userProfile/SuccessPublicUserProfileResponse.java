package org.deus.src.responses.api.userProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.userProfile.PublicUserProfileDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessPublicUserProfileResponse {
    private PublicUserProfileDTO userProfile;
}
