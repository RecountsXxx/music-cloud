package org.deus.src.models.base;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@MappedSuperclass
@Data
public class BaseId {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;
}
