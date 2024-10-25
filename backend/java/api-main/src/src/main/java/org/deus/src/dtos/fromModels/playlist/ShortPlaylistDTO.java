package org.deus.src.dtos.fromModels.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortPlaylistDTO {
    private String id;
    private ShortUserProfileDTO creatorUserProfile;
    private String name;
    private ImageUrlsDTO cover;
    private LocalDateTime createdAt;
}
