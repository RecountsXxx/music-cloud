package org.deus.src.models.intermediateTables.baseClasses;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.ListenerModel;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.base.BaseIdCreate;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class BaseListenerRelease extends BaseIdCreate {
    @ManyToOne
    @JoinColumn(name = "listener_id", nullable = false)
    private ListenerModel listener;

    @ManyToOne
    @JoinColumn(name = "release_id", nullable = false)
    private ReleaseModel release;
}
