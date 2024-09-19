package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.enums.Privacy;
import org.deus.src.models.base.BaseIdCreateUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(name = "number_of_songs")
    private Short numberOfSongs;

    @Column(name = "duration")
    private Float duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy", length = 7)
    private Privacy privacy;
}
