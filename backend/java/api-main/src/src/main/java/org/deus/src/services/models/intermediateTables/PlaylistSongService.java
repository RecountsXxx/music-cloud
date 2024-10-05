package org.deus.src.services.models.intermediateTables;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.playlist.ShortPlaylistDTO;
import org.deus.src.dtos.fromModels.song.ShortSongDTO;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.PlaylistModel;
import org.deus.src.models.ReleaseModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.UserProfileModel;
import org.deus.src.models.intermediateTables.PlaylistSongModel;
import org.deus.src.repositories.PlaylistRepository;
import org.deus.src.repositories.SongRepository;
import org.deus.src.repositories.intermediateTables.PlaylistSongRepository;
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
import static org.deus.src.services.models.SongService.getShortSongDTO;

@Service
@RequiredArgsConstructor
public class PlaylistSongService {
    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    @Cacheable(value = "song_playlists", key = "#songId")
    public List<ShortPlaylistDTO> getPlaylistsBySongId(UUID songId) throws DataNotFoundException {
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        return playlistSongRepository
                .findBySong(song).stream()
                .map(playlistSongModel -> {
                    PlaylistModel playlist = playlistSongModel.getPlaylist();
                    UserProfileModel creatorUserProfile = playlist.getCreatorUserProfile();

                    return getShortPlaylistDTO(playlist, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "playlist_songs", key = "#playlistId")
    public List<ShortSongDTO> getSongsByPlaylistId(UUID playlistId) throws DataNotFoundException {
        PlaylistModel playlist = playlistRepository
                .findById(playlistId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));

        return playlistSongRepository
                .findByPlaylist(playlist).stream()
                .map(playlistSongModel -> {
                    SongModel song = playlistSongModel.getSong();
                    ReleaseModel release = song.getRelease();
                    UserProfileModel creatorUserProfile = release.getCreatorUserProfile();

                    return getShortSongDTO(song, release, creatorUserProfile, imageService);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "song_playlists", key = "#songId"),
                    @CacheEvict(value = "playlist_songs", key = "#playlistId")
            }
    )
    public void addSongToPlaylist(UUID playlistId, UUID songId) throws DataNotFoundException, ActionCannotBePerformedException {
        PlaylistModel playlist = playlistRepository
                .findById(playlistId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        if (playlistSongRepository.findByPlaylistAndSong(playlist, song).isPresent()) {
            throw new ActionCannotBePerformedException("Song is already added to this playlist");
        }

        PlaylistSongModel playlistSong = new PlaylistSongModel();
        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);
        playlistSongRepository.save(playlistSong);

        playlist.setDuration(playlist.getDuration() + song.getDuration());
        playlist.setNumberOfSongs((short) (playlist.getNumberOfSongs() + 1));
        playlistRepository.save(playlist);

        song.setNumberOfPlaylistsWhichContainsSong(song.getNumberOfPlaylistsWhichContainsSong() + 1);
        songRepository.save(song);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "song_playlists", key = "#songId"),
                    @CacheEvict(value = "playlist_songs", key = "#playlistId")
            }
    )
    public void removeSongFromPlaylist(UUID playlistId, UUID songId) throws DataNotFoundException, ActionCannotBePerformedException {
        PlaylistModel playlist = playlistRepository
                .findById(playlistId)
                .orElseThrow(() -> new DataNotFoundException("Playlist not found"));
        SongModel song = songRepository
                .findById(songId)
                .orElseThrow(() -> new DataNotFoundException("Song not found"));

        PlaylistSongModel playlistSong = playlistSongRepository
                .findByPlaylistAndSong(playlist, song)
                .orElseThrow(() -> new ActionCannotBePerformedException("Song is not in the playlist"));

        playlistSongRepository.delete(playlistSong);

        playlist.setDuration(playlist.getDuration() - song.getDuration());
        playlist.setNumberOfSongs((short) (playlist.getNumberOfSongs() - 1));
        playlistRepository.save(playlist);

        song.setNumberOfPlaylistsWhichContainsSong(song.getNumberOfPlaylistsWhichContainsSong() - 1);
        songRepository.save(song);
    }
}
