package org.deus.src.models.intermediateTables.reposts;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.intermediateTables.baseClasses.BaseUserProfileSong;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_profile_reposted_songs")
public class UserProfileRepostedSongModel extends BaseUserProfileSong {

}
