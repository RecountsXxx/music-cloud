package org.deus.src.services.storage;

import org.deus.src.drivers.StorageDriverInterface;
import org.deus.src.enums.ImageSize;
import org.springframework.stereotype.Service;

@Service
public class StorageCoverService  extends StorageImageServiceBase {
    public StorageCoverService(StorageDriverInterface storage) {
        super(storage, "covers");
    }

    @Override
    protected String buildPathToOriginalBytes(String collectionId) {
        return "/" + collectionId + "/originalBytes";
    }

    @Override
    protected String buildPathToFile(String collectionId, ImageSize size) {
        return collectionId + "/collection-cover-" + size.toString() + ".webp";
    }
}
