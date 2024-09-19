package org.deus.src.models.intermediateTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.intermediateTables.baseClasses.BaseListenerPlaylist;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "listener_reposted_playlists")
public class ListenerRepostedPlaylistModel extends BaseListenerPlaylist {

}
