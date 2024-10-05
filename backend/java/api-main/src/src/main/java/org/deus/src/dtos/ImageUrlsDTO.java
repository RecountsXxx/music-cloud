package org.deus.src.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUrlsDTO {
    private String small;
    private String medium;
    private String large;
}
