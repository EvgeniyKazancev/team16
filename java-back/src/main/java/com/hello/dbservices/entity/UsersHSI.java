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
public class UsersHSI {
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

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<UserSessions> userSessions;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    public UsersHSI(){
        this.created = LocalDateTime.now();
    }

    @Column(name = "admin", columnDefinition = "TINYINT(1)", nullable = false, length = 1)
    private boolean isAdmin;

    @Column(name = "superuser", columnDefinition = "TINYINT(1)", nullable = false, length = 1)
    private boolean isSuperUser;



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
