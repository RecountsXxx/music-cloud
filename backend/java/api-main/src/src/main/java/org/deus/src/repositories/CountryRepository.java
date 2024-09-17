package org.deus.src.repositories;

import org.deus.src.models.CountryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryModel, Short> {
}
