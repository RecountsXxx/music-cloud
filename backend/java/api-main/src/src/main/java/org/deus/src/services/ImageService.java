package org.deus.src.services;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.ImageUrlsDTO;
import org.deus.src.enums.ImageSize;
import org.deus.src.services.storage.StorageAvatarService;
import org.deus.src.services.storage.StorageCoverService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final StorageCoverService storageCoverService;
    private final StorageAvatarService storageAvatarService;

    public ImageUrlsDTO getCoverForCollection (String collectionId) {
        return new ImageUrlsDTO(
                storageCoverService.getPathToFile(collectionId, ImageSize.SMALL),
                storageCoverService.getPathToFile(collectionId, ImageSize.MEDIUM),
                storageCoverService.getPathToFile(collectionId, ImageSize.LARGE)
        );
    }

    public ImageUrlsDTO getAvatarForUser (String userId) {
        return new ImageUrlsDTO(
                storageAvatarService.getPathToFile(userId, ImageSize.SMALL),
                storageAvatarService.getPathToFile(userId, ImageSize.MEDIUM),
                storageAvatarService.getPathToFile(userId, ImageSize.LARGE)
        );
    }
}
