package org.deus.src.dtos.fromModels.userProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileActionDTO {
    private ShortUserProfileDTO userProfile;
    private LocalDateTime createdAt;
}
