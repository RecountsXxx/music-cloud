package org.deus.src.models.intermediateTables.baseClasses;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deus.src.models.ListenerModel;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.base.BaseIdCreate;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class BaseListenerPlaylist extends BaseIdCreate {
    @ManyToOne
    @JoinColumn(name = "listener_id", nullable = false)
    private ListenerModel listener;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private PlaylistModel playlist;
}
