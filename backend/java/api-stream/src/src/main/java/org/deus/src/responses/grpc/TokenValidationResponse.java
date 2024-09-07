package org.deus.src.responses.grpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse {
    private String tokenStatus;
    private UserDTO userDTO;
}
