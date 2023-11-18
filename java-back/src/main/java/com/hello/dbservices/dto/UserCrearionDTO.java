package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserCrearionDTO implements Serializable {
    private String currentUUID;
    private String email;
    private String firstName;
    private String lastName;
    private String patronym;
    private String password;
    private boolean isAdmin;
    private boolean isSuperUser;
}
