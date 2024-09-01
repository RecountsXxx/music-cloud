package org.deus.src.dtos.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvatarsReadyDTO {
    private String small;
    private String medium;
    private String large;
}
