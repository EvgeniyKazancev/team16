package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "source_restricted_keywords", schema = "test", catalog = "")
public class SourceRestrictedKeywords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "source_id")
    private long sourceId;

    @Column(name = "keyword")
    private String keyword;


//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null ) return false;
//        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
//        if (thisEffectiveClass != oEffectiveClass) return false;
//        SourceRestrictedKeywords sourceRestrictedKeywords = (SourceRestrictedKeywords) o;
//        return getId() != null && Objects.equals(getId(),sourceRestrictedKeywords.getId()) && Objects.equals(getSourceId(),sourceRestrictedKeywords.getSourceId())
//                               && Objects.equals(getKeyword(),sourceRestrictedKeywords.getKeyword());
//
//    }
//
//    @Override
//    public final int hashCode() {
//        return this instanceof HibernateProxy
//                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
//                : getClass().hashCode();
//    }
}
