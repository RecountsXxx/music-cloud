package org.deus.src.dtos.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.enums.ContentType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionBaseDTO {
    private Object content;
    private ContentType contentType;
    private LocalDateTime createdAt;
}
