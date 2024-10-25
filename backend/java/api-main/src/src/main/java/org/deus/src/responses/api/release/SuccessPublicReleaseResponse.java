package org.deus.src.responses.api.release;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.release.PublicReleaseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessPublicReleaseResponse {
    private PublicReleaseDTO release;
}
