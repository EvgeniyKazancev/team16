package com.hello.util;

import com.hello.dbservices.dto.UserCrearionDTO;
import com.hello.dbservices.dto.UserUpdateRegularDTO;
import com.hello.dbservices.dto.UserUpdateSuperDTO;
import com.hello.dbservices.entity.Users;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class UserMapper {
    public Users userCreation2User(UserCrearionDTO userDTO) throws NoSuchAlgorithmException {
        return new Users(userDTO.getEmail(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPatronym(),
                MD5Calculation.getMD5(userDTO.getPassword()),
                userDTO.isAdmin(),
                userDTO.isSuperUser());
    }

    public Users userUpdateRegular2User(Users user, UserUpdateRegularDTO userDTO) throws NoSuchAlgorithmException {
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPatronym(userDTO.getPatronym());
        user.setPasswordHash(MD5Calculation.getMD5(userDTO.getPassword()));

        return user;
    }

    public Users userUpdateSuper2User(Users user, UserUpdateSuperDTO userDTO) throws NoSuchAlgorithmException {
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPatronym(userDTO.getPatronym());
        user.setPasswordHash(MD5Calculation.getMD5(userDTO.getPassword()));
        user.setAdmin(userDTO.isAdmin());
        user.setSuperUser(userDTO.isSuperUser());

        return user;
    }
}
