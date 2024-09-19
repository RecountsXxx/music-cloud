package org.deus.src.models.intermediateTables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.ListenerModel;
import org.deus.src.models.SongModel;
import org.deus.src.models.intermediateTables.baseClasses.BaseListenerSong;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "listener_listened_history")
public class ListenerListenedHistoryModel extends BaseListenerSong {

}
