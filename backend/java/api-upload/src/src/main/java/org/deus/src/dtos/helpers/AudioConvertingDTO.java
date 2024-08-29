package org.deus.src.dtos.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioConvertingDTO {
    private String userId;
    private String audioId;
    private String fileId;
}