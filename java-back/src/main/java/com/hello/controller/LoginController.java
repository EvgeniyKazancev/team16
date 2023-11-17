package com.hello.controller;

import com.hello.dbservices.dto.LoginDTO;
import com.hello.dbservices.entity.Users;
import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UsersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/")
@RestController
public class LoginController {

    private final UsersServices usersServices;

    @Autowired
    public LoginController(UsersServices usersServices) {
        this.usersServices = usersServices;
    }

    @PutMapping("/login")
    public ResponseMessage login(@RequestBody LoginDTO login) {

        return new ResponseMessage("---" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    @PutMapping("/2auth")
    public String confirm2auth(@RequestBody String confirm2auth) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
