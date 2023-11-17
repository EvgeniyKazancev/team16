package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "test")
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronym")
    private String patronym;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "created")
    private LocalDateTime created;
    public Users(){
        this.created = LocalDateTime.now();
    }



//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null ) return false;
//        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
//        if (thisEffectiveClass != oEffectiveClass) return false;
//        Users users = (Users) o;
//        return getId() != null && Objects.equals(getId(),users.getId()) && Objects.equals(getEmail(),users.getEmail()) && Objects.equals(getFirstName(),users.getFirstName())
//                && Objects.equals(getLastName(),users.getLastName()) && Objects.equals(getPatronym(),users.getPatronym()) && Objects.equals(getPasswordHash(),users.getPasswordHash());
//    }
//
//    @Override
//    public final int hashCode() {
//        return this instanceof HibernateProxy
//                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
//                : getClass().hashCode();
//    }
}
