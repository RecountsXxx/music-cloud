package org.deus.src.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.models.base.BaseIdCreate;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentModel extends BaseIdCreate {
    @Column(name = "content", length = 300, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private SongModel song;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private ListenerModel creator;
}
