package org.deus.src.requests.userProfile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.enums.AudioQuality;
import org.deus.src.enums.Gender;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateRequest {
    @NotNull(message = "The id cannot be null")
    @NotBlank(message = "The id cannot be empty")
    private String id;

    private String displayName;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Gender gender;
    private AudioQuality preferredQuality;
    private String biography;
    private Short countryId;
}
