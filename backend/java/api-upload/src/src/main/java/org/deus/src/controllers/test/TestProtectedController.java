package org.deus.src.controllers.test;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/upload/protected/test")
@RequiredArgsConstructor
public class TestProtectedController {
    private static final Logger logger = LoggerFactory.getLogger(TestProtectedController.class);

    @GetMapping("/hello-world")
    public ResponseEntity<String> helloWorld(@RequestAttribute("userDTO") UserDTO userDTO) {
        return ResponseEntity.ok("Hello " + userDTO.getUsername());
    }
}
