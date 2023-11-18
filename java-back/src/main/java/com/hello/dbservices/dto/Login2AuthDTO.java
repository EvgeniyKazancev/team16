package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Login2AuthDTO implements Serializable {
    String uuid;
    String confirm2auth;
}
