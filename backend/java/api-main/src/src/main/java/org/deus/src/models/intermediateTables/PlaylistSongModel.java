package org.deus.src.models.intermediateTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.base.BaseIdCreate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "playlist_songs")
public class PlaylistSongModel extends BaseIdCreate {
    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private PlaylistModel playlist;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private SongModel song;
}
