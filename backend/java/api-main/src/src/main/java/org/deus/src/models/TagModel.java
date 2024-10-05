package org.deus.src.models;

import java.util.HashSet;
import java.util.Set;

import org.deus.src.dtos.fromModels.tag.ShortTagDTO;
import org.deus.src.dtos.fromModels.tag.TagDTO;
import org.deus.src.models.base.BaseIdCreate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "tags")
public class TagModel extends BaseIdCreate {
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<SongModel> songs = new HashSet<>();



    public static TagDTO toDTO(TagModel model) {
        return new TagDTO(
                model.getId().toString(),
                model.getName(),
                model.getCreatedAt()
        );
    }

    public static ShortTagDTO toShortDTO(TagModel model) {
        return new ShortTagDTO(
                model.getId().toString(),
                model.getName()
        );
    }
}
