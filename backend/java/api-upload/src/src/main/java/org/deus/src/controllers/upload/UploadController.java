package org.deus.src.controllers.upload;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.responses.SuccessCheckFileResponse;
import org.deus.src.responses.SuccessRequestFileIdResponse;
import org.deus.src.services.tusUpload.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/upload/protected/audio")
public class UploadController {
    private final UploadService uploadService;
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @RequestMapping(
            value = {"/file", "/file/**"},
            method = {
                    RequestMethod.POST, RequestMethod.PATCH,
                    RequestMethod.HEAD, RequestMethod.DELETE,
                    RequestMethod.GET
            })
    public void upload(@RequestAttribute("userDTO") UserDTO userDTO, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
        this.uploadService.processUpload(servletRequest, servletResponse, userDTO.getId());
    }

    @PostMapping("/request-file-id")
    public ResponseEntity<SuccessRequestFileIdResponse> requestUpload() {
        String fileId = UUID.randomUUID().toString();
        SuccessRequestFileIdResponse response = new SuccessRequestFileIdResponse(fileId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-file/{fileId}")
    public ResponseEntity<SuccessCheckFileResponse> checkFile(@RequestAttribute("userDTO") UserDTO userDTO, @PathVariable String fileId) throws StatusException {
        Boolean isFileExists = uploadService.checkFile(userDTO.getId(), fileId);
        SuccessCheckFileResponse response = new SuccessCheckFileResponse(isFileExists);
        return ResponseEntity.ok(response);
    }

    @Scheduled(fixedDelayString = "PT24H")
    protected void cleanup() {
        this.uploadService.cleanup();
    }
}
