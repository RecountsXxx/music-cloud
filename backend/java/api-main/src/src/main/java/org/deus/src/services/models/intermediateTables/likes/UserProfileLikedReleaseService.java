package org.deus.src.services.models.intermediateTables.likes;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.likes.UserProfileLikedReleaseModel;
import org.deus.src.repositories.ReleaseRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.likes.UserProfileLikedReleaseRepository;
import org.deus.src.services.ImageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.ReleaseService.getShortReleaseDTO;
import static org.deus.src.services.models.UserProfileService.getShortUserProfileDTO;

@Service
@RequiredArgsConstructor
public class UserProfileLikedReleaseService {
    private final UserProfileLikedReleaseRepository userProfileLikedReleaseRepository;
    private final ReleaseRepository releaseRepository;
    private final UserProfileRepository userProfileRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles_liked_release", key = "#contentId")
    public List<ShortUserProfileDTO> getUserProfilesThatLikedContent(UUID contentId) throws DataNotFoundException {
        ReleaseModel release = releaseRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        return userProfileLikedReleaseRepository
                .findByRelease(release).stream()
                .map(userProfileLikedReleaseModel -> getShortUserProfileDTO(userProfileLikedReleaseModel.getUserProfile(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "releases_liked_by_user_profile", key = "#userProfileId")
    public List<ShortReleaseDTO> getLikedContent(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        return userProfileLikedReleaseRepository
                .findByUserProfile(userProfile).stream()
                .map(userProfileLikedReleaseModel -> {
                    ReleaseModel release = userProfileLikedReleaseModel.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortReleaseDTO(release, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_liked_release", key = "#contentId"),
                    @CacheEvict(value = "releases_liked_by_user_profile", key = "#userProfileId")
            }
    )
    public void likeContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        ReleaseModel release = releaseRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        if (userProfileLikedReleaseRepository.findByUserProfileAndRelease(userProfile, release).isPresent()) {
            throw new ActionCannotBePerformedException("Already liked this release");
        }

        UserProfileLikedReleaseModel userProfileLikedRelease = new UserProfileLikedReleaseModel();
        userProfileLikedRelease.setUserProfile(userProfile);
        userProfileLikedRelease.setRelease(release);
        userProfileLikedReleaseRepository.save(userProfileLikedRelease);

        userProfile.setNumberOfLikedReleases(userProfile.getNumberOfLikedReleases() + 1);
        userProfileRepository.save(userProfile);

        release.setNumberOfLikes(release.getNumberOfLikes() + 1);
        releaseRepository.save(release);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_liked_release", key = "#contentId"),
                    @CacheEvict(value = "releases_liked_by_user_profile", key = "#userProfileId")
            }
    )
    public void removeLikeFromContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        ReleaseModel release = releaseRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        UserProfileLikedReleaseModel userProfileLikedRelease = userProfileLikedReleaseRepository.findByUserProfileAndRelease(userProfile, release)
                .orElseThrow(() -> new ActionCannotBePerformedException("Release is not liked"));

        userProfileLikedReleaseRepository.delete(userProfileLikedRelease);

        userProfile.setNumberOfLikedReleases(userProfile.getNumberOfLikedReleases() - 1);
        userProfileRepository.save(userProfile);

        release.setNumberOfLikes(release.getNumberOfLikes() - 1);
        releaseRepository.save(release);
    }
}
