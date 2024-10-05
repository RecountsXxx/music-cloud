package org.deus.src.models.intermediateTables.baseClasses;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.base.BaseIdCreate;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class BaseUserProfilePlaylist extends BaseIdCreate {
    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfileModel userProfile;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private PlaylistModel playlist;
}
