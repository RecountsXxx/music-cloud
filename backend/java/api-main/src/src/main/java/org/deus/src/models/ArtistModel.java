package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.base.BaseIdUpdate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artists")
public class ArtistModel extends BaseIdUpdate {
    @Column(name = "user_id", columnDefinition = "UUID", nullable = false)
    private UUID userId;

    @Column(name = "monthly_listeners")
    private Integer monthlyListeners;

    @Column(name = "number_of_releases")
    private Integer numberOfReleases;

    @OneToMany(mappedBy = "artist")
    private Set<ReleaseModel> releases = new HashSet<>();
}
