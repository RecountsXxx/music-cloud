package org.deus.src.services.models.intermediateTables;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.UserBlockModel;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.UserBlockRepository;
import org.deus.src.services.ImageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.UserProfileService.getShortUserProfileDTO;

@Service
@RequiredArgsConstructor
public class UserBlockService {
    private final UserBlockRepository userBlockRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserFollowingService userFollowingService;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_blocks", key = "#id")
    public List<ShortUserProfileDTO> getBlockedUsers(UUID id) throws DataNotFoundException {
        UserProfileModel user = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return userBlockRepository
                .findByBlocker(user).stream()
                .map(userBlockModel -> getShortUserProfileDTO(userBlockModel.getBlocked(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_blocks", key = "#blockerId")
            }
    )
    public void blockUser(UUID blockerId, UUID blockedId) throws DataNotFoundException, ActionCannotBePerformedException {
        if (blockerId.equals(blockedId)) {
            throw new ActionCannotBePerformedException("A user cannot block themselves");
        }

        UserProfileModel blocker = userProfileRepository
                .findById(blockerId)
                .orElseThrow(() -> new DataNotFoundException("Blocker not found"));
        UserProfileModel blocked = userProfileRepository
                .findById(blockedId)
                .orElseThrow(() -> new DataNotFoundException("Blocked user not found"));

        if (userBlockRepository.findByBlockerAndBlocked(blocker, blocked).isPresent()) {
            throw new ActionCannotBePerformedException("User is already blocked");
        }

        UserBlockModel userBlock = new UserBlockModel();
        userBlock.setBlocker(blocker);
        userBlock.setBlocked(blocked);
        userBlockRepository.save(userBlock);

        // Unfollow each other if they were following
        try {
            userFollowingService.unfollowUser(blockerId, blockedId);
        } catch (RuntimeException e) {
            // Ignore if they weren't following
        }
        try {
            userFollowingService.unfollowUser(blockedId, blockerId);
        } catch (RuntimeException e) {
            // Ignore if they weren't following
        }

        blocker.setNumberOfBlockedUsers(blocker.getNumberOfBlockedUsers() + 1);

        userProfileRepository.save(blocker);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_blocks", key = "#blockerId")
            }
    )
    public void unblockUser(UUID blockerId, UUID blockedId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel blocker = userProfileRepository
                .findById(blockerId)
                .orElseThrow(() -> new DataNotFoundException("Blocker not found"));
        UserProfileModel blocked = userProfileRepository
                .findById(blockedId)
                .orElseThrow(() -> new DataNotFoundException("Blocked user not found"));

        UserBlockModel userBlock = userBlockRepository
                .findByBlockerAndBlocked(blocker, blocked)
                .orElseThrow(() -> new ActionCannotBePerformedException("User is not blocked"));

        userBlockRepository.delete(userBlock);

        blocker.setNumberOfBlockedUsers(blocker.getNumberOfBlockedUsers() - 1);

        userProfileRepository.save(blocker);
    }
}
