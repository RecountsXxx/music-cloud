package org.deus.src.requests.userProfile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCreateRequest {
    @NotNull(message = "The userId cannot be null")
    @NotBlank(message = "The userId cannot be empty")
    private String userId;

    @NotNull(message = "The username cannot be null")
    @NotBlank(message = "The username cannot be empty")
    private String username;

    @NotNull(message = "The displayName cannot be null")
    @NotBlank(message = "The displayName cannot be empty")
    private String displayName;
}
