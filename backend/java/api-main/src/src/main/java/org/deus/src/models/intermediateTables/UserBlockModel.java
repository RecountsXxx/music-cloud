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
@Table(name = "user_blocks")
public class UserBlockModel extends BaseIdCreate {
    @ManyToOne
    @JoinColumn(name = "blocker_id", nullable = false)
    private UserProfileModel blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_id", nullable = false)
    private UserProfileModel blocked;
}
