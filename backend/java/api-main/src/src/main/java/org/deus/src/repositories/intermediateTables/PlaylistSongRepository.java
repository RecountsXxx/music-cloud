package org.deus.src.repositories.intermediateTables;

import org.deus.src.models.PlaylistModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.intermediateTables.PlaylistSongModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSongModel, UUID> {
    Optional<PlaylistSongModel> findByPlaylistAndSong(PlaylistModel playlist, SongModel song);
    List<PlaylistSongModel> findByPlaylist(PlaylistModel playlist);
    List<PlaylistSongModel> findBySong(SongModel song);
}
