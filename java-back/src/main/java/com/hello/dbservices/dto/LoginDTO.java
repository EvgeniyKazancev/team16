package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    String email;
    String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
