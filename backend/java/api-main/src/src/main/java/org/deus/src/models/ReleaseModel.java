package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.dtos.fromModels.release.ReleaseDTO;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.enums.ReleaseType;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedReleaseModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedReleaseModel;

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
    private Set<UserProfileLikedReleaseModel> listenersLiked = new HashSet<>();

    @OneToMany(mappedBy = "release")
    private Set<UserProfileRepostedReleaseModel> listenersReposted = new HashSet<>();



    public static ReleaseDTO toDTO(ReleaseModel model, ShortUserProfileDTO creatorUserProfileDTO, ImageUrlsDTO cover) {
        return new ReleaseDTO(
                model.getId().toString(),
                model.getName(),
                model.getDuration(),
                model.getPrivacy(),
                model.getNumberOfSongs(),
                cover,
                model.getCreatedAt(),
                model.getUpdatedAt(),
                creatorUserProfileDTO,
                model.getReleaseDate(),
                model.getType(),
                model.getBuyLink(),
                model.getRecordLabel()
        );
    }

    public static ShortReleaseDTO toShortDTO(ReleaseModel model, ShortUserProfileDTO creatorUserProfileDTO, ImageUrlsDTO cover) {
        return new ShortReleaseDTO(
                model.getId().toString(),
                creatorUserProfileDTO,
                model.getName(),
                cover
        );
    }
}
