package com.hello.controller;

import com.hello.dbservices.dto.UserCrearionDTO;
import com.hello.dbservices.dto.UserUpdateRegularDTO;
import com.hello.dbservices.dto.UserUpdateSuperDTO;
import com.hello.dbservices.entity.Users;
import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UsersServices;
import com.hello.dbservices.services.UsersServicesHSI;
import com.hello.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersServices usersServices;
    private final UsersServicesHSI usersServicesHSI;
    private final UserMapper userMapper;

    @Autowired
    public UsersController(UsersServices usersServices, UsersServicesHSI usersServicesHSI, UserMapper userMapper) {
        this.usersServices = usersServices;
        this.usersServicesHSI = usersServicesHSI;
        this.userMapper = userMapper;
    }

    @GetMapping("/getUserId")
    public UsersHSI getUser(@RequestParam Long userId, String uuid) {
        UsersHSI usersHSI = usersServicesHSI.getUser(userId, uuid);
        if (usersHSI.getId() == null)
            throw new ResponseStatusException(HttpStatusCode.valueOf(403));
        return usersHSI;
    }

    @GetMapping("/getAllUsers")
    public List<UsersHSI> getAllUsers(@RequestParam String uuid) {
        List<UsersHSI> usersHSIList = usersServicesHSI.getAllUsers(uuid);
        if (usersHSIList.isEmpty())
            throw new ResponseStatusException(HttpStatusCode.valueOf(403));
        return usersHSIList;
    }

    @PostMapping(value = "/addUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseMessage addUser(UserCrearionDTO userCreation) throws NoSuchAlgorithmException {
        Users user = usersServices.getUsers(usersServices.getUserSessionByUuid(userCreation.getCurrentUUID()).getUserId());
        if (user.isSuperUser() && user.getUserSessions().stream().anyMatch(s -> s.getUuid().equals(userCreation.getCurrentUUID()))) {

            return usersServices.addUser(userMapper.userCreation2User(userCreation));
        }
        return new ResponseMessage("Недостаточно прав или пользователь не залогинен", ResponseType.FORBIDDEN.getCode());
    }

    @PostMapping(value = "/updateUserRegular", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseMessage updateUser(UserUpdateRegularDTO userUpdate) throws NoSuchAlgorithmException {
        Users user = usersServices.getUsers(usersServices.getUserSessionByUuid(userUpdate.getCurrentUUID()).getUserId());
        if (Objects.equals(user.getId(), userUpdate.getId()))
            return  usersServices.updateUser(userMapper.userUpdateRegular2User(user, userUpdate));
        return new ResponseMessage("Попытка изменить чужую учётную запись", ResponseType.FORBIDDEN.getCode());
    }

    @PostMapping(value = "/updateUserSuper", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseMessage updateUserSuper(UserUpdateSuperDTO userUpdate) throws NoSuchAlgorithmException {
        Users user = usersServices.getUsers(usersServices.getUserSessionByUuid(userUpdate.getCurrentUUID()).getUserId());
        if (Objects.equals(user.getId(), userUpdate.getId()) && user.isSuperUser())
            return  usersServices.updateUser(userMapper.userUpdateSuper2User(user, userUpdate));
        return new ResponseMessage("Попытка изменить чужую учётную запись или недостаточно прав", ResponseType.FORBIDDEN.getCode());
    }
}
