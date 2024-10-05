package org.deus.src.models.intermediateTables;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.intermediateTables.baseClasses.BaseUserProfileSong;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_profile_listened_history")
public class UserProfileListenedHistoryModel extends BaseUserProfileSong {

}
