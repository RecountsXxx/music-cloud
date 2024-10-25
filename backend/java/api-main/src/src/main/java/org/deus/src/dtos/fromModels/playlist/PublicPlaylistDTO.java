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
public class PublicPlaylistDTO {
    private String id;
    private String name;
    private Float duration;
    private Short numberOfSongs;
    private ImageUrlsDTO cover;
    private ShortUserProfileDTO creatorUserProfile;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
