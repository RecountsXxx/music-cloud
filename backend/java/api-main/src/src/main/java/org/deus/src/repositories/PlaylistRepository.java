package org.deus.src.repositories;

import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<PlaylistModel, UUID> {
    List<PlaylistModel> findAllByCreatorUserProfile(UserProfileModel creatorUserProfile);
}
