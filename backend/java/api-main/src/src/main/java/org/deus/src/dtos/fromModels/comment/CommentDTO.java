package org.deus.src.dtos.fromModels.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String id;
    private ShortUserProfileDTO creatorUserProfile;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
