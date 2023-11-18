package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users_sessions")
public class UserSessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "session_authorized", columnDefinition = "TINYINT(1)", nullable = false, length = 1)
    private Boolean isAuthorized;

    @Column(name = "one_time_token", nullable = false)
    private String oneTimeToken;

    @Column(name = "token_created", nullable = false)
    private LocalDateTime tokenCreated;

    public UserSessions() {
        this.created = LocalDateTime.now();
        this.lastUpdate = LocalDateTime.now();
        this.isAuthorized = false;
    }
}
