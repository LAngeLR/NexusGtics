package com.example.nexusgtics.controllers.Email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class EmailController {

    Authenticate authenticate;

    public EmailController(Authenticate authenticate) {
        this.authenticate = authenticate;
    }

    @PostMapping("/prueba")
    public ResponseEntity<Object> authenticate(@RequestBody LoginRequest loginRequest){
        authenticate.sendMessageUser(loginRequest.getEmailUser(), loginRequest.getMessage());
        return ResponseEntity.ok()
                .body("Hola ...");
    }
}
