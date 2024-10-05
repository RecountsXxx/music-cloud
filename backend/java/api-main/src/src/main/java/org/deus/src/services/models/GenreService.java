package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.genre.GenreDTO;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.GenreModel;
import org.deus.src.repositories.GenreRepository;
import org.deus.src.requests.genre.GenreCreateRequest;
import org.deus.src.requests.genre.GenreUpdateRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    @Cacheable(value = "genres_pageable", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<GenreDTO> getAll(Pageable pageable) {
        return genreRepository
                .findAll(pageable)
                .map(GenreModel::toDTO);
    }

    @Cacheable(value = "genres")
    public List<GenreModel> getAll() {
        return new ArrayList<>(genreRepository
                .findAll());
    }

    @Cacheable(value = "genres_dto")
    public List<GenreDTO> getAllDTOs() {
        return getAll().stream()
                .map(GenreModel::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "genre", key = "#id")
    public GenreModel getById(Short id) throws DataNotFoundException {
        return genreRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Genre not found"));
    }

    @Cacheable(value = "genre_dto", key = "#id")
    public GenreDTO getDTOById(Short id) throws DataNotFoundException {
        return GenreModel.toDTO(getById(id));
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"genres_pageable", "genres", "genres_dto"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "genre", key = "#result.id")
            }
    )
    public GenreModel create(GenreCreateRequest request) {
        GenreModel genre = new GenreModel();
        genre.setName(request.getName());

        return genreRepository.save(genre);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"genres_pageable", "genres", "genres_dto", "song_genres"}, allEntries = true),
                    @CacheEvict(value = "genre_dto", key = "#result.id")
            },
            put = {
                    @CachePut(value = "genre", key = "#result.id")
            }
    )
    public GenreModel update(GenreUpdateRequest request) throws DataNotFoundException {
        GenreModel genre = genreRepository.findById(request.getId()).orElseThrow(() -> new DataNotFoundException("Genre not found"));

        if (request.getName() != null) {
            genre.setName(request.getName());
        }

        return genreRepository.save(genre);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = {"genres_pageable", "genres", "genres_dto"}, allEntries = true),
            @CacheEvict(value = "genre", key = "#id"),
            @CacheEvict(value = "genre_dto", key = "#id")
    })
    public void delete(Short id) {
        genreRepository.deleteById(id);
    }
}
