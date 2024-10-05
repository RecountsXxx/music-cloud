package org.deus.src.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.dtos.fromModels.song.SongDTO;
import org.deus.src.enums.AudioStatus;
import org.deus.src.models.base.BaseIdCreateUpdate;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedSongModel;
import org.deus.src.models.intermediateTables.UserProfileListenedHistoryModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedSongModel;
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

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "release_id", nullable = false)
    private ReleaseModel release;

    @Column(name = "place_number", columnDefinition = "SMALLINT")
    private Short placeNumber;

    @Column(name = "duration")
    private Float duration = 0.0f;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private AudioStatus status;

    @Column(name = "number_of_plays")
    private Integer numberOfPlays = 0;

    @Column(name = "number_of_likes")
    private Integer numberOfLikes = 0;

    @Column(name = "number_of_reposts")
    private Integer numberOfReposts = 0;

    @Column(name = "number_of_comments")
    private Integer numberOfComments = 0;

    @Column(name = "number_of_playlists_which_contains_song")
    private Integer numberOfPlaylistsWhichContainsSong = 0;



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
    private Set<UserProfileListenedHistoryModel> userProfilesListenedHistory = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<UserProfileLikedSongModel> userProfilesLiked = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<UserProfileRepostedSongModel> userProfilesReposted = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private Set<PlaylistSongModel> songInPlaylists = new HashSet<>();



    public static SongDTO toDTO(SongModel model, ShortReleaseDTO releaseDTO) {
        return new SongDTO(
                model.getId().toString(),
                model.getName(),
                releaseDTO,
                model.getPlaceNumber(),
                model.getDuration(),
                model.getStatus(),
                model.getNumberOfPlays()
        );
    }

    public static ShortSongDTO toShortDTO(SongModel model, ShortReleaseDTO releaseDTO) {
        return new ShortSongDTO(
                model.getId().toString(),
                model.getName(),
                releaseDTO,
                model.getPlaceNumber(),
                model.getDuration(),
                model.getNumberOfPlays()
        );
    }
}
