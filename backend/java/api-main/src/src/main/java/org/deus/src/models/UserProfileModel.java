package org.deus.src.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.dtos.fromModels.userProfile.UserProfileDTO;
import org.deus.src.enums.AudioQuality;
import org.deus.src.enums.Gender;
import org.deus.src.models.base.BaseIdUpdate;
import org.deus.src.models.intermediateTables.UserBlockModel;
import org.deus.src.models.intermediateTables.UserFollowingModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.intermediateTables.UserProfileListenedHistoryModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedPlaylistModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedReleaseModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedSongModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedPlaylistModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedReleaseModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedSongModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfileModel extends BaseIdUpdate {
    @Column(name = "user_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "display_name", length = 50, nullable = false)
    private String displayName;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 6)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_quality", length = 6)
    private AudioQuality preferredQuality;

    @Column(name = "biography", length = 400)
    private String biography;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryModel country;

    @Column(name = "number_of_followers")
    private Integer numberOfFollowers = 0;

    @Column(name = "number_of_followings")
    private Integer numberOfFollowings = 0;

    @Column(name = "number_of_blocked_users")
    private Integer numberOfBlockedUsers = 0;

    @Column(name = "number_of_liked_playlists")
    private Integer numberOfLikedPlaylists = 0;

    @Column(name = "number_of_liked_releases")
    private Integer numberOfLikedReleases = 0;

    @Column(name = "number_of_liked_songs")
    private Integer numberOfLikedSongs = 0;

    @Column(name = "number_of_reposted_playlists")
    private Integer numberOfRepostedPlaylists = 0;

    @Column(name = "number_of_reposted_releases")
    private Integer numberOfRepostedReleases = 0;

    @Column(name = "number_of_reposted_songs")
    private Integer numberOfRepostedSongs = 0;

    @Column(name = "monthly_listeners")
    private Integer monthlyListeners = 0;

    @Column(name = "number_of_releases")
    private Short numberOfReleases = 0;

    @Column(name = "number_of_created_playlists")
    private Short numberOfCreatedPlaylists = 0;



    @OneToMany(mappedBy = "creatorUserProfile")
    private Set<ReleaseModel> releases = new HashSet<>();

    @OneToMany(mappedBy = "creatorUserProfile")
    private Set<PlaylistModel> playlists = new HashSet<>();

    @OneToMany(mappedBy = "creatorUserProfile")
    private Set<CommentModel> comments = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    private Set<UserProfileListenedHistoryModel> songsListenedHistory = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    private Set<UserProfileLikedReleaseModel> releasesLiked = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    private Set<UserProfileRepostedReleaseModel> releasesReposted = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    private Set<UserProfileLikedPlaylistModel> playlistsLiked = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    private Set<UserProfileRepostedPlaylistModel> playlistsReposted = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    private Set<UserProfileLikedSongModel> songsLiked = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    private Set<UserProfileRepostedSongModel> songsReposted = new HashSet<>();



    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserFollowingModel> followings = new HashSet<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserFollowingModel> followers = new HashSet<>();

    @OneToMany(mappedBy = "blocker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBlockModel> blockedUsers = new HashSet<>();

    @OneToMany(mappedBy = "blocked", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBlockModel> blockedByUsers = new HashSet<>();



    public static UserProfileDTO toDTO(UserProfileModel model, ImageUrlsDTO avatar) {
        return new UserProfileDTO(
                model.getId().toString(),
                model.getUsername(),
                model.getDisplayName(),
                avatar,
                model.getFirstName(),
                model.getLastName(),
                model.getBirthday(),
                model.getGender(),
                model.getPreferredQuality(),
                model.getBiography(),
                model.getCountry() != null ? model.getCountry().getId() : null,
                model.getNumberOfFollowers(),
                model.getNumberOfFollowings(),
                model.getNumberOfBlockedUsers()
        );
    }

    public static ShortUserProfileDTO toShortDTO(UserProfileModel model, ImageUrlsDTO avatar) {
        return new ShortUserProfileDTO(
                model.getId().toString(),
                model.getUsername(),
                model.getDisplayName(),
                avatar
        );
    }
}
