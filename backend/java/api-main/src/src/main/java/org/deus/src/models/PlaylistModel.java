package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.intermediateTables.ListenerLikedPlaylistModel;
import org.deus.src.models.intermediateTables.ListenerRepostedPlaylistModel;
import org.deus.src.models.intermediateTables.PlaylistSongModel;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class PlaylistModel extends CollectionModel {
    @Column(name = "description", length = 300)
    private String description;

    @OneToMany(mappedBy = "playlist")
    private Set<PlaylistSongModel> playlistSongs = new HashSet<>();

    @OneToMany(mappedBy = "playlist")
    private Set<ListenerRepostedPlaylistModel> listenersReposted = new HashSet<>();

    @OneToMany(mappedBy = "playlist")
    private Set<ListenerLikedPlaylistModel> listenersLiked = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private ListenerModel creator;
}
