package com.hello.controller;

import com.hello.dbservices.dto.Login2AuthDTO;
import com.hello.dbservices.dto.LoginDTO;
import com.hello.dbservices.entity.UserSessions;
import com.hello.dbservices.entity.Users;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UserSessionServices;
import com.hello.dbservices.services.UsersServices;
import com.hello.util.MD5Calculation;
import com.hello.util.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UsersServices usersServices;
    private final UserSessionServices userSessionServices;

    @Autowired
    public LoginController(UsersServices usersServices, UserSessionServices userSessionServices) {
        this.usersServices = usersServices;
        this.userSessionServices = userSessionServices;
    }

    @PostMapping(value = "/auth")
    public ResponseMessage login(LoginDTO login) throws NoSuchAlgorithmException {
        String passHash = MD5Calculation.getMD5(login.getPassword());
        Users usr = usersServices.getByEmail(login.getEmail());
        if (Objects.equals(usr.getPasswordHash(), passHash)) {

            UUID uuid = UUID.randomUUID();
            Integer oneTimeToken = new Random().nextInt(900000) + 100000;

            if (SendEmail.sendToken(usr.getEmail(), oneTimeToken.toString()))
                userSessionServices.addUserSession(usr.getId(), uuid.toString(), oneTimeToken.toString());
            else
                return new ResponseMessage("Send one-time token has failed", 500);

            return new ResponseMessage(uuid.toString(), ResponseType.OPERATION_SUCCESSFUL.getCode());
        } else {
            return new ResponseMessage("Login denied", 403);
        }
    }

    @PostMapping(value = "/2auth")
    public ResponseMessage confirm2auth(Login2AuthDTO confirm2auth) {

        UserSessions userSession = usersServices.getUserSessionByUuid(confirm2auth.getUuid());
        if (userSession.getOneTimeToken().equals(confirm2auth.getConfirm2auth()))
            if (!userSession.getIsAuthorized())
                if (ChronoUnit.MINUTES.between(LocalDateTime.now(), userSession.getTokenCreated()) < 10) {
                    userSession.setIsAuthorized(true);
                    userSessionServices.saveUserSession(userSession);
                    return new ResponseMessage(confirm2auth.getUuid(), ResponseType.OPERATION_SUCCESSFUL.getCode());
                }
        return new ResponseMessage("Login denied", 403);
    }
}
