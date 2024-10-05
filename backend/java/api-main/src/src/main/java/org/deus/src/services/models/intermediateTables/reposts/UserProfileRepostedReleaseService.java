package org.deus.src.services.models.intermediateTables.reposts;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.release.ShortReleaseDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedReleaseModel;
import org.deus.src.repositories.ReleaseRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.reposts.UserProfileRepostedReleaseRepository;
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
public class UserProfileRepostedReleaseService {
    private final UserProfileRepostedReleaseRepository userProfileRepostedReleaseRepository;
    private final ReleaseRepository releaseRepository;
    private final UserProfileRepository userProfileRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles_reposted_release", key = "#contentId")
    public List<ShortUserProfileDTO> getUserProfilesThatRepostedContent(UUID contentId) throws DataNotFoundException {
        ReleaseModel release = releaseRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        return userProfileRepostedReleaseRepository
                .findByRelease(release).stream()
                .map(userProfileRepostedReleaseModel -> getShortUserProfileDTO(userProfileRepostedReleaseModel.getUserProfile(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "releases_reposted_by_user_profile", key = "#userProfileId")
    public List<ShortReleaseDTO> getRepostedContent(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        return userProfileRepostedReleaseRepository
                .findByUserProfile(userProfile).stream()
                .map(userProfileRepostedReleaseModel -> {
                    ReleaseModel release = userProfileRepostedReleaseModel.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortReleaseDTO(release, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_reposted_release", key = "#contentId"),
                    @CacheEvict(value = "releases_reposted_by_user_profile", key = "#userProfileId")
            }
    )
    public void repostContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        ReleaseModel release = releaseRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        if (userProfileRepostedReleaseRepository.findByUserProfileAndRelease(userProfile, release).isPresent()) {
            throw new ActionCannotBePerformedException("Already reposted this release");
        }

        UserProfileRepostedReleaseModel userProfileRepostedRelease = new UserProfileRepostedReleaseModel();
        userProfileRepostedRelease.setUserProfile(userProfile);
        userProfileRepostedRelease.setRelease(release);
        userProfileRepostedReleaseRepository.save(userProfileRepostedRelease);

        userProfile.setNumberOfRepostedReleases(userProfile.getNumberOfRepostedReleases() + 1);
        userProfileRepository.save(userProfile);

        release.setNumberOfReposts(release.getNumberOfReposts() + 1);
        releaseRepository.save(release);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_reposted_release", key = "#contentId"),
                    @CacheEvict(value = "releases_reposted_by_user_profile", key = "#userProfileId")
            }
    )
    public void removeRepostOfContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        ReleaseModel release = releaseRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Release not found"));

        UserProfileRepostedReleaseModel userProfileRepostedRelease = userProfileRepostedReleaseRepository
                .findByUserProfileAndRelease(userProfile, release)
                .orElseThrow(() -> new ActionCannotBePerformedException("Release is not reposted"));

        userProfileRepostedReleaseRepository.delete(userProfileRepostedRelease);

        userProfile.setNumberOfRepostedReleases(userProfile.getNumberOfRepostedReleases() - 1);
        userProfileRepository.save(userProfile);

        release.setNumberOfReposts(release.getNumberOfReposts() - 1);
        releaseRepository.save(release);
    }
}
