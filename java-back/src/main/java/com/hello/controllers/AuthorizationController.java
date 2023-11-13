package com.hello.controllers;

import com.hello.models.LoginFormMain;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorizationController {

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody LoginFormMain loginFormMain) {
        return loginFormMain.getEmail() + loginFormMain.getPassword();
    }
}
