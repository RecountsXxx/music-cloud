package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.enums.Gender;
import org.deus.src.enums.AudioQuality;
import org.deus.src.models.base.BaseIdUpdate;
import org.deus.src.models.intermediateTables.UserBlockModel;
import org.deus.src.models.intermediateTables.UserFollowingModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfileModel extends BaseIdUpdate {
    @Column(name = "user_id", columnDefinition = "UUID", nullable = false)
    private UUID userId;

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

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserFollowingModel> followings = new HashSet<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserFollowingModel> followers = new HashSet<>();

    @OneToMany(mappedBy = "blocker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBlockModel> blockedUsers = new HashSet<>();

    @OneToMany(mappedBy = "blocked", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBlockModel> blockedByUsers = new HashSet<>();

    @Column(name = "number_of_followers")
    private Integer numberOfFollowers;

    @Column(name = "number_of_followings")
    private Integer numberOfFollowings;

    @Column(name = "number_of_blocked_users")
    private Integer numberOfBlockedUsers;
}