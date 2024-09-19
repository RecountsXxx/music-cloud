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
@Table(name = "listener_liked_playlists")
public class ListenerLikedPlaylistModel extends BaseListenerPlaylist {

}
