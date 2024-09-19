package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.base.BaseIdUpdate;
import org.deus.src.models.intermediateTables.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listeners")
public class ListenerModel extends BaseIdUpdate {
    @Column(name = "user_id", columnDefinition = "UUID", nullable = false)
    private UUID userId;

    @Column(name = "number_of_liked_playlists")
    private Integer numberOfLikedPlaylists;

    @Column(name = "number_of_liked_releases")
    private Integer numberOfLikedReleases;

    @Column(name = "number_of_liked_songs")
    private Integer numberOfLikedSongs;

    @OneToMany(mappedBy = "creator")
    private Set<CommentModel> comments = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<PlaylistModel> createdPlaylists = new HashSet<>();

    @OneToMany(mappedBy = "listener")
    private Set<ListenerLikedReleaseModel> likedReleases = new HashSet<>();

    @OneToMany(mappedBy = "listener")
    private Set<ListenerRepostedReleaseModel> repostedReleases = new HashSet<>();

    @OneToMany(mappedBy = "listener")
    private Set<ListenerListenedHistoryModel> songsListenedHistory = new HashSet<>();

    @OneToMany(mappedBy = "listener")
    private Set<ListenerLikedSongModel> likedSongs = new HashSet<>();

    @OneToMany(mappedBy = "listener")
    private Set<ListenerRepostedSongModel> repostedSongs = new HashSet<>();

    @OneToMany(mappedBy = "listener")
    private Set<ListenerRepostedPlaylistModel> repostedPlaylists = new HashSet<>();

    @OneToMany(mappedBy = "listener")
    private Set<ListenerLikedPlaylistModel> likedPlaylists = new HashSet<>();
}
