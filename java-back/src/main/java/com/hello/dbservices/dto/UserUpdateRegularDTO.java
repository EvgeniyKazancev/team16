package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserUpdateRegularDTO implements Serializable {
    private String currentUUID;
    private Long id;
    private String firstName;
    private String lastName;
    private String patronym;
    private String password;
}
