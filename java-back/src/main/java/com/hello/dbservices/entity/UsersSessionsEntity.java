package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users_sessions", schema = "test", catalog = "")
public class UsersSessionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id",nullable = false)
    private Users userId;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersSessionsEntity that = (UsersSessionsEntity) o;
        return id == that.id && userId == that.userId && Objects.equals(uuid, that.uuid) && Objects.equals(created, that.created) && Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, uuid, created, lastUpdate);
    }
}
