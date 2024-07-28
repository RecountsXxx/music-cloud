package org.deus.src.controllers.profile;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.services.profile.AvatarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("java/protected-api/profile/avatar")
@RequiredArgsConstructor
public class AvatarUploadController {
    private final AvatarService avatarService;

    @PostMapping
    public ResponseEntity<String> uploadAvatar(@RequestAttribute("userDTO") UserDTO userDTO, @RequestParam("avatar") MultipartFile avatar) throws StatusException {
        if (avatar.isEmpty()) {
            return new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST);
        }

        return avatarService.avatarUpload(avatar, userDTO);
    }
}
