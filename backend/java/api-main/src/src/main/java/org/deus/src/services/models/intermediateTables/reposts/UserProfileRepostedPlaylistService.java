package org.deus.src.services.models.intermediateTables.reposts;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.userProfile.ShortUserProfileDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.reposts.UserProfileRepostedPlaylistModel;
import org.deus.src.repositories.PlaylistRepository;
import org.deus.src.repositories.UserProfileRepository;
import org.deus.src.repositories.intermediateTables.reposts.UserProfileRepostedPlaylistRepository;
import org.deus.src.services.ImageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.deus.src.services.models.PlaylistService.getShortPlaylistDTO;
import static org.deus.src.services.models.UserProfileService.getShortUserProfileDTO;

@Service
@RequiredArgsConstructor
public class UserProfileRepostedPlaylistService {
    private final UserProfileRepostedPlaylistRepository userProfileRepostedPlaylistRepository;
    private final PlaylistRepository playlistRepository;
    private final UserProfileRepository userProfileRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "user_profiles_reposted_playlist", key = "#contentId")
    public List<ShortUserProfileDTO> getUserProfilesThatRepostedContent(UUID contentId) throws DataNotFoundException {
        PlaylistModel playlist = playlistRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        return userProfileRepostedPlaylistRepository
                .findByPlaylist(playlist).stream()
                .map(userProfileRepostedPlaylistModel -> getShortUserProfileDTO(userProfileRepostedPlaylistModel.getUserProfile(), imageService))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "playlists_reposted_by_user_profile", key = "#userProfileId")
    public List<ShortPlaylistDTO> getRepostedContent(UUID userProfileId) throws DataNotFoundException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));

        return userProfileRepostedPlaylistRepository
                .findByUserProfile(userProfile).stream()
                .map(userProfileRepostedPlaylistModel -> {
                    PlaylistModel playlist = userProfileRepostedPlaylistModel.getPlaylist();
                    UserProfileModel creatorUserProfile = playlist.getCreatorUserProfile();

                    return getShortPlaylistDTO(playlist, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_reposted_playlist", key = "#contentId"),
                    @CacheEvict(value = "playlists_reposted_by_user_profile", key = "#userProfileId")
            }
    )
    public void repostContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        PlaylistModel playlist = playlistRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        if(userProfileRepostedPlaylistRepository.findByUserProfileAndPlaylist(userProfile, playlist).isPresent()) {
            throw new ActionCannotBePerformedException("Already reposted this playlist");
        }

        UserProfileRepostedPlaylistModel userProfileRepostedPlaylist = new UserProfileRepostedPlaylistModel();
        userProfileRepostedPlaylist.setUserProfile(userProfile);
        userProfileRepostedPlaylist.setPlaylist(playlist);
        userProfileRepostedPlaylistRepository.save(userProfileRepostedPlaylist);

        userProfile.setNumberOfRepostedPlaylists(userProfile.getNumberOfRepostedPlaylists() + 1);
        userProfileRepository.save(userProfile);

        playlist.setNumberOfReposts(playlist.getNumberOfReposts() + 1);
        playlistRepository.save(playlist);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "user_profiles_reposted_playlist", key = "#contentId"),
                    @CacheEvict(value = "playlists_reposted_by_user_profile", key = "#userProfileId")
            }
    )
    public void removeRepostOfContent(UUID userProfileId, UUID contentId) throws DataNotFoundException, ActionCannotBePerformedException {
        UserProfileModel userProfile = userProfileRepository
                .findById(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User Profile not found"));
        PlaylistModel playlist = playlistRepository
                .findById(contentId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        UserProfileRepostedPlaylistModel userProfileRepostedPlaylist = userProfileRepostedPlaylistRepository
                .findByUserProfileAndPlaylist(userProfile, playlist)
                .orElseThrow(() -> new ActionCannotBePerformedException("Playlist is not reposted"));

        userProfileRepostedPlaylistRepository.delete(userProfileRepostedPlaylist);

        userProfile.setNumberOfRepostedPlaylists(userProfile.getNumberOfRepostedPlaylists() - 1);
        userProfileRepository.save(userProfile);

        playlist.setNumberOfReposts(playlist.getNumberOfReposts() - 1);
        playlistRepository.save(playlist);
    }
}
