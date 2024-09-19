package org.deus.src.models.intermediateTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.intermediateTables.baseClasses.BaseListenerRelease;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "listener_liked_releases")
public class ListenerLikedReleaseModel extends BaseListenerRelease {

}
