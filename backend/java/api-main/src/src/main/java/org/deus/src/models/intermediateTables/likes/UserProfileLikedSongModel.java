package org.deus.src.models.intermediateTables.likes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.intermediateTables.baseClasses.BaseUserProfileSong;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_profile_liked_songs")
public class UserProfileLikedSongModel extends BaseUserProfileSong {

}
