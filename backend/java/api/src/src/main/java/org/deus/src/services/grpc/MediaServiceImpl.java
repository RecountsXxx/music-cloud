package org.deus.src.services.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import media.Media;
import media.MediaServiceGrpc;
import org.deus.src.enums.ImageSize;
import org.deus.src.services.storage.StorageAvatarService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl extends MediaServiceGrpc.MediaServiceImplBase {
    private final StorageAvatarService storageAvatarService;

    @Override
    public void getAvatars(Media.GetAvatarsRequest request, StreamObserver<Media.GetAvatarsResponse> responseObserver) {
        String userId = request.getUserId();

        String smallAvatar = storageAvatarService.getPathToFile(userId, ImageSize.SMALL);
        String mediumAvatar = storageAvatarService.getPathToFile(userId, ImageSize.MEDIUM);
        String largeAvatar = storageAvatarService.getPathToFile(userId, ImageSize.LARGE);

        Media.AvatarSet avatarSet = Media.AvatarSet.newBuilder()
                .setSmall(smallAvatar)
                .setMedium(mediumAvatar)
                .setLarge(largeAvatar)
                .build();

        Media.GetAvatarsResponse response = Media.GetAvatarsResponse.newBuilder()
                .setAvatars(avatarSet)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
