package org.deus.src.models.intermediateTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.base.BaseIdCreate;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_followings")
public class UserFollowingModel extends BaseIdCreate {
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private UserProfileModel follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private UserProfileModel following;
}
