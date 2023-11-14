package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "category_restricted_keywords", schema = "test", catalog = "")
public class CategoryRestrictedKeywords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id",nullable = false)
    private Categories categoriesId;

    @Column(name = "keyword")
    private String keyword;



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CategoryRestrictedKeywords categoryRestrictedKeywords = (CategoryRestrictedKeywords) o;
        return getId() != null && Objects.equals(getId(),categoryRestrictedKeywords.getId()) && Objects.equals(getKeyword(),categoryRestrictedKeywords.getCategoriesId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
