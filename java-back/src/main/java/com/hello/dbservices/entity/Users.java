package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class Users{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<UserSessions> userSessions;

    @Column(name = "admin", columnDefinition = "TINYINT(1)", nullable = false, length = 1)
    private boolean isAdmin;

    @Column(name = "superuser", columnDefinition = "TINYINT(1)", nullable = false, length = 1)
    private boolean isSuperUser;

    public Users(String email,
                 String firstName,
                 String lastName,
                 String patronym,
                 String passwordHash,
                 Boolean isAdmin,
                 Boolean isSuperUser) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronym = patronym;
        this.passwordHash = passwordHash;
        this.created = LocalDateTime.now();
        this.isAdmin = isAdmin;
        this.isSuperUser = isSuperUser;
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
