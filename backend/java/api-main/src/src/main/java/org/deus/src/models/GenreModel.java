package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.genre.GenreDTO;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genres")
public class GenreModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "SMALLINT")
    private Short id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;



    @ManyToMany(mappedBy = "genres")
    private Set<SongModel> songs = new HashSet<>();



    public static GenreDTO toDTO(GenreModel model) {
        return new GenreDTO(
                model.getId(),
                model.getName()
        );
    }
}
