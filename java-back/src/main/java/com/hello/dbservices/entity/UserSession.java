package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users_sessions")
public class UserSession {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}
