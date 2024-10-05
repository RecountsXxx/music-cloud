package org.deus.src.repositories;

import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<ReleaseModel, UUID> {
    List<ReleaseModel> findAllByCreatorUserProfile(UserProfileModel creatorUserProfile);
}
