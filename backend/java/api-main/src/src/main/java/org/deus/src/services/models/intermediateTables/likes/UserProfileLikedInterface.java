package org.deus.src.services.models.intermediateTables.likes;

import org.deus.src.dtos.actions.LikeContentDTO;
import org.deus.src.dtos.actions.UserProfileLikedRepostedDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserProfileLikedInterface {
    List<UserProfileLikedRepostedDTO> getUserProfilesThatLikedContent(UUID contentId) throws DataNotFoundException;
    List<LikeContentDTO> getLikedContent(UUID userProfileId) throws DataNotFoundException;
    void likeContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException;
    void removeLikeFromContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException;
}
