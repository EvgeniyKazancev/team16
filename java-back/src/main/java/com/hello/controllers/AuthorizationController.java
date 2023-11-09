package com.hello.controllers;

import com.hello.models.LoginFormMain;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorizationController {

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json")
    public String login(@RequestBody LoginFormMain loginFormMain) {
        return loginFormMain.toString();
    }
}
