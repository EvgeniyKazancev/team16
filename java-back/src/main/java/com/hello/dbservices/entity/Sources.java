package com.hello.dbservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "sources", schema = "test", catalog = "")
public class Sources {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
 
    @Column(name = "url")
    private String url;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "parse_depth",columnDefinition = "INTEGER DEFAULT 2")
    @Size(min = 1,max = 4)
    private Integer parseDepth;

    @Column(name = "created",nullable = false)
    private LocalDateTime created;

    public Sources() {
    }

//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null ) return false;
//        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
//        if (thisEffectiveClass != oEffectiveClass) return false;
//        Sources sources = (Sources) o;
//        return getId() != null && Objects.equals(getId(),sources.getId()) && Objects.equals(getUrl(),sources.getUrl()) && Objects.equals(getSourceType(),sources.getSourceType())
//                && Objects.equals(getParseDepth(),sources.getParseDepth()) ;
//    }
//
//    @Override
//    public final int hashCode() {
//        return this instanceof HibernateProxy
//                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
//                : getClass().hashCode();
//    }
}
