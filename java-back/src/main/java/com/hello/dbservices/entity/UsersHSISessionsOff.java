package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "test")
public class UsersHSISessionsOff {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "patronym")
    private String patronym;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    public UsersHSISessionsOff(){
        this.created = LocalDateTime.now();
    }

    @Column(name = "admin", columnDefinition = "TINYINT(1)", nullable = false, length = 1)
    private boolean isAdmin;

    @Column(name = "superuser", columnDefinition = "TINYINT(1)", nullable = false, length = 1)
    private boolean isSuperUser;
}
