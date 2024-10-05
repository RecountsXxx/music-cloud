package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.comment.CommentDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.models.base.BaseIdCreateUpdate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentModel extends BaseIdCreateUpdate {
    @Column(name = "content", length = 300, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private SongModel song;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfileModel creatorUserProfile;



    public static CommentDTO toDTO(CommentModel model, ShortUserProfileDTO creatorUserProfileDTO) {
        return new CommentDTO(
                model.getId().toString(),
                creatorUserProfileDTO,
                model.getContent(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }
}
