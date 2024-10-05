package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.enums.Privacy;
import org.deus.src.models.base.BaseIdCreateUpdate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "collections")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CollectionModel extends BaseIdCreateUpdate {
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "duration")
    private Float duration = 0.0f;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy", length = 7)
    private Privacy privacy;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfileModel creatorUserProfile;

    @Column(name = "number_of_songs")
    private Short numberOfSongs = 0;

    @Column(name = "number_of_likes")
    private Integer numberOfLikes = 0;

    @Column(name = "number_of_reposts")
    private Integer numberOfReposts = 0;
}
