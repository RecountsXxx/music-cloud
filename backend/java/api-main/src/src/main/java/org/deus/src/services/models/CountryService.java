package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.country.CountryDTO;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.CountryModel;
import org.deus.src.repositories.CountryRepository;
import org.deus.src.requests.country.CountryCreateRequest;
import org.deus.src.requests.country.CountryUpdateRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    @Cacheable(value = "countries")
    public List<CountryDTO> getAllDTOs() {
        return countryRepository
                .findAll().stream()
                .map(CountryModel::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "country", key = "#id")
    public CountryModel getById(Short id) throws DataNotFoundException {
        return countryRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Country not found"));
    }

    @Cacheable(value = "country_dto", key = "#id")
    public CountryDTO getDTOById(Short id) throws DataNotFoundException {
        return CountryModel.toDTO(getById(id));
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "countries", allEntries = true)
            },
            put = {
                    @CachePut(value = "country", key = "#result.id"),
                    @CachePut(value = "country_dto", key = "#result.id")
            }
    )
    public CountryModel create(CountryCreateRequest request) {
        CountryModel country = new CountryModel();
        country.setName(request.getName());

        return countryRepository.save(country);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "countries", allEntries = true)
            },
            put = {
                    @CachePut(value = "country", key = "#result.id"),
                    @CachePut(value = "country_dto", key = "#result.id")
            }
    )
    public CountryModel update(CountryUpdateRequest request) throws DataNotFoundException {
        CountryModel country = countryRepository
                .findById(request.getId())
                .orElseThrow(() -> new DataNotFoundException("Country not found"));

        if (request.getName() != null) {
            country.setName(request.getName());
        }

        return countryRepository.save(country);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "countries", allEntries = true),
            @CacheEvict(value = "country", key = "#id"),
            @CacheEvict(value = "country_dto", key = "#id")
    })
    public void delete(Short id) {
        countryRepository.deleteById(id);
    }
}
