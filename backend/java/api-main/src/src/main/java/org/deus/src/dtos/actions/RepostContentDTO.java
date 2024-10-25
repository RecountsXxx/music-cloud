package org.deus.src.dtos.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.deus.src.enums.ContentType;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class RepostContentDTO extends ActionBaseDTO {
    public RepostContentDTO (Object content, ContentType contentType, LocalDateTime createdAt) {
        super(content, contentType, createdAt);
    }
}
