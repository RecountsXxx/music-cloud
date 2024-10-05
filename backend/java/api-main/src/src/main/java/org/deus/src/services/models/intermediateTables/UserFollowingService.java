package org.deus.src.services.models.intermediateTables;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.UserFollowingModel;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.UserFollowingRepository;
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
public class UserFollowingService {
    private final UserFollowingRepository userFollowingRepository;
    private final UserProfileRepository userProfileRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_followers", key = "#id")
    public List<ShortUserProfileDTO> getFollowers(UUID id) throws DataNotFoundException {
        UserProfileModel user = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return userFollowingRepository
                .findByFollowing(user).stream()
                .map(userFollowingModel -> getShortUserProfileDTO(userFollowingModel.getFollower(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user_followings", key = "#id")
    public List<ShortUserProfileDTO> getFollowings(UUID id) throws DataNotFoundException {
        UserProfileModel user = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return userFollowingRepository
                .findByFollower(user).stream()
                .map(userFollowingModel -> getShortUserProfileDTO(userFollowingModel.getFollowing(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_followers", key = "#followingId"),
                    @CacheEvict(value = "user_followings", key = "#followerId")
            }
    )
    public void followUser(UUID followerId, UUID followingId) throws DataNotFoundException, ActionCannotBePerformedException {
        if (followerId.equals(followingId)) {
            throw new ActionCannotBePerformedException("A user cannot follow themselves");
        }

        UserProfileModel follower = userProfileRepository
                .findById(followerId)
                .orElseThrow(() -> new DataNotFoundException("Follower not found"));
        UserProfileModel following = userProfileRepository
                .findById(followingId)
                .orElseThrow(() -> new DataNotFoundException("Following user not found"));

        if (userFollowingRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            throw new ActionCannotBePerformedException("Already following this user");
        }

        UserFollowingModel userFollowing = new UserFollowingModel();
        userFollowing.setFollower(follower);
        userFollowing.setFollowing(following);
        userFollowingRepository.save(userFollowing);

        follower.setNumberOfFollowings(follower.getNumberOfFollowings() + 1);
        following.setNumberOfFollowers(following.getNumberOfFollowers() + 1);

        userProfileRepository.save(follower);
        userProfileRepository.save(following);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_followers", key = "#followingId"),
                    @CacheEvict(value = "user_followings", key = "#followerId")
            }
    )
    public void unfollowUser(UUID followerId, UUID followingId) throws DataNotFoundException, ActionCannotBePerformedException {
        if (followerId.equals(followingId)) {
            throw new ActionCannotBePerformedException("A user cannot unfollow themselves");
        }

        UserProfileModel follower = userProfileRepository
                .findById(followerId)
                .orElseThrow(() -> new DataNotFoundException("Follower not found"));
        UserProfileModel following = userProfileRepository
                .findById(followingId)
                .orElseThrow(() -> new DataNotFoundException("Following user not found"));

        UserFollowingModel userFollowing = userFollowingRepository
                .findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new ActionCannotBePerformedException("Not following this user"));

        userFollowingRepository.delete(userFollowing);

        follower.setNumberOfFollowings(follower.getNumberOfFollowings() - 1);
        following.setNumberOfFollowers(following.getNumberOfFollowers() - 1);

        userProfileRepository.save(follower);
        userProfileRepository.save(following);
    }
}
