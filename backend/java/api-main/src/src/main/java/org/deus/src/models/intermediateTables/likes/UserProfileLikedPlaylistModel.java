package org.deus.src.models.intermediateTables.likes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.intermediateTables.baseClasses.BaseUserProfilePlaylist;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_profile_liked_playlists")
public class UserProfileLikedPlaylistModel extends BaseUserProfilePlaylist {

}
