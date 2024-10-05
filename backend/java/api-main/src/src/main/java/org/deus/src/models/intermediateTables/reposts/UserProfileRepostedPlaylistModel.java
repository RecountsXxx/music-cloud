package org.deus.src.models.intermediateTables.reposts;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.intermediateTables.baseClasses.BaseUserProfilePlaylist;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_profile_reposted_playlists")
public class UserProfileRepostedPlaylistModel extends BaseUserProfilePlaylist {

}
