package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class LoginDTO implements Serializable {
    String email;
    String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
