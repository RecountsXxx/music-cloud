package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.enums.ReleaseType;
import org.deus.src.models.intermediateTables.ListenerLikedReleaseModel;
import org.deus.src.models.intermediateTables.ListenerRepostedReleaseModel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "releases")
public class ReleaseModel extends CollectionModel {
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistModel artist;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 6)
    private ReleaseType type;

    @Column(name = "buy_link", length = 300)
    private String buyLink;

    @Column(name = "record_label", length = 300)
    private String recordLabel;

    @OneToMany(mappedBy = "release")
    private Set<SongModel> songs = new HashSet<>();

    @OneToMany(mappedBy = "release")
    private Set<ListenerLikedReleaseModel> listenersLiked = new HashSet<>();

    @OneToMany(mappedBy = "release")
    private Set<ListenerRepostedReleaseModel> listenersReposted = new HashSet<>();
}
