package org.deus.src.services.storage;

import org.deus.src.drivers.StorageDriverInterface;
import org.deus.src.enums.ImageSize;
import org.springframework.stereotype.Service;

@Service
public class StorageAvatarService extends StorageImageServiceBase {
    public StorageAvatarService(StorageDriverInterface storage) {
        super(storage, "avatars");
    }

    @Override
    protected String buildPathToOriginalBytes(String userId) {
        return "/" + userId + "/originalBytes";
    }

    @Override
    protected String buildPathToFile(String userId, ImageSize size) {
        return userId + "/avatar-" + size.toString() + ".webp";
    }
}