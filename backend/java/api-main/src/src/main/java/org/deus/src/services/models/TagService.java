package org.deus.src.services.models;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.tag.TagDTO;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.TagModel;
import org.deus.src.repositories.TagRepository;
import org.deus.src.requests.tag.TagUpdateRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Cacheable(value = "tags_pageable", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<TagDTO> getAll(Pageable pageable) {
        return tagRepository
                .findAll(pageable)
                .map(TagModel::toDTO);
    }

    @Cacheable(value = "tag", key = "#id")
    public TagModel getById(UUID id) throws DataNotFoundException {
        return tagRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Tag not found"));
    }

    @Cacheable(value = "tag_dto", key = "#id")
    public TagDTO getDTOById(UUID id) throws DataNotFoundException {
        return TagModel.toDTO(getById(id));
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"tags_pageable"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "tag", key = "#result.id")
            }
    )
    public TagModel create(String tagName) {
        TagModel tag = new TagModel();
        tag.setName(tagName);

        return tagRepository.save(tag);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"tags_pageable", "song_tags"}, allEntries = true),
                    @CacheEvict(value = "tag_dto", key = "#result.id")
            },
            put = {
                    @CachePut(value = "tag", key = "#result.id")
            }
    )
    public TagModel update(TagUpdateRequest request) throws DataNotFoundException {
        TagModel tag = tagRepository.findById(UUID.fromString(request.getId())).orElseThrow(() -> new DataNotFoundException("Tag not found"));

        if (request.getName() != null) {
            tag.setName(request.getName());
        }

        return tagRepository.save(tag);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"tags_pageable", "song_tags"}, allEntries = true),
                    @CacheEvict(value = {"tag", "tag_dto"}, key = "#id")
            }
    )
    public void delete(UUID id) {
        tagRepository.deleteById(id);
    }
}
