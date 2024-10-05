package org.deus.src.models.intermediateTables;

import org.deus.src.models.UserProfileModel;
import org.deus.src.models.base.BaseIdCreate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
