package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.country.CountryDTO;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
public class CountryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "SMALLINT")
    private Short id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;



    @OneToMany(mappedBy = "country")
    private Set<UserProfileModel> userProfiles = new HashSet<>();



    public static CountryDTO toDTO(CountryModel model) {
        return new CountryDTO(
                model.getId(),
                model.getName()
        );
    }
}
