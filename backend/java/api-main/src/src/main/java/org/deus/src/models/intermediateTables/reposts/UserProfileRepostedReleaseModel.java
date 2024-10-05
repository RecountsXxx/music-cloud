package org.deus.src.models.intermediateTables.reposts;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.intermediateTables.baseClasses.BaseUserProfileRelease;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_profile_reposted_releases")
public class UserProfileRepostedReleaseModel extends BaseUserProfileRelease {

}
