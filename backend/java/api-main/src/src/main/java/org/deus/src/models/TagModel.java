package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.base.BaseIdCreate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class TagModel extends BaseIdCreate {
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<SongModel> songs = new HashSet<>();
}
