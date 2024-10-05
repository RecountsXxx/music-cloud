package org.deus.src.dtos.fromModels.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private String id;
    private String name;
    private LocalDateTime createdAt;
}
