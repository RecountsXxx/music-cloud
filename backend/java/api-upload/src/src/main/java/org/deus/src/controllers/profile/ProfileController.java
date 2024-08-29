package org.deus.src.controllers.profile;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.responses.SuccessMessageResponse;
import org.deus.src.services.profile.AvatarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload/protected/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final AvatarService avatarService;

    @PostMapping(value = "/avatar")
    public ResponseEntity<SuccessMessageResponse> avatarUpload(@RequestAttribute("userDTO") UserDTO userDTO, @RequestParam("avatar") MultipartFile avatar) throws StatusException {
        if (avatar.isEmpty()) {
            throw new StatusException("Please select a file!", HttpStatus.BAD_REQUEST);
        }

        return avatarService.avatarUpload(userDTO, avatar);
    }

    @PostMapping(value = "/gravatar")
    public ResponseEntity<SuccessMessageResponse> gravatarUpdate(@RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        return avatarService.gravatarUpdate(userDTO);
    }
}
