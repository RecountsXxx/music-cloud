package org.deus.src.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.deus.src.enums.AudioStatus;
import org.deus.src.models.base.BaseIdCreateUpdate;
import org.deus.src.models.intermediateTables.ListenerLikedSongModel;
import org.deus.src.models.intermediateTables.ListenerListenedHistoryModel;
import org.deus.src.models.intermediateTables.ListenerRepostedSongModel;
import org.deus.src.models.intermediateTables.PlaylistSongModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class SongModel extends BaseIdCreateUpdate {
    @Column(name = "temp_file_id", columnDefinition = "UUID")
    private UUID tempFileId;

    @ManyToOne
    @JoinColumn(name = "release_id", nullable = false)
    private ReleaseModel release;

    @Column(name = "place_number", columnDefinition = "SMALLINT")
    private Short placeNumber;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "duration")
    private Float duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private AudioStatus status;

    @ManyToMany
    @JoinTable(
            name = "song_genres",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<GenreModel> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "song_tags",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagModel> tags = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<CommentModel> comments = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<ListenerListenedHistoryModel> listenersListenedHistory = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<ListenerLikedSongModel> listenersLiked = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<ListenerRepostedSongModel> listenersReposted = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<PlaylistSongModel> playlistSongs = new HashSet<>();
}
