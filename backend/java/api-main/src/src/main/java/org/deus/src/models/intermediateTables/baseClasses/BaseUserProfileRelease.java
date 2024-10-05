package org.deus.src.models.intermediateTables.baseClasses;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.base.BaseIdCreate;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class BaseUserProfileRelease extends BaseIdCreate {
    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfileModel userProfile;

    @ManyToOne
    @JoinColumn(name = "release_id", nullable = false)
    private ReleaseModel release;
}
