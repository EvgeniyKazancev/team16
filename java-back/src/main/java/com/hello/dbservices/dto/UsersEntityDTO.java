package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UsersEntityDTO implements Serializable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String patronym;
    private String passwordHash;
    private LocalDateTime created;
}
