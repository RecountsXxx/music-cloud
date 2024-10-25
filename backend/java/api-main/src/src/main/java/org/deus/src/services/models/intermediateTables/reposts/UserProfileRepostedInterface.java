package org.deus.src.services.models.intermediateTables.reposts;

import org.deus.src.dtos.actions.RepostContentDTO;
import org.deus.src.dtos.actions.UserProfileLikedRepostedDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserProfileRepostedInterface {
    List<UserProfileLikedRepostedDTO> getUserProfilesThatRepostedContent(UUID contentId) throws DataNotFoundException;
    List<RepostContentDTO> getRepostedContent(UUID userProfileId) throws DataNotFoundException;
    void repostContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException;
    void removeRepostOfContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException;
}
