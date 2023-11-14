package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "publications_categories", schema = "test", catalog = "")
public class PublicationsCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publications publicationId;

    @ManyToOne(optional = false)
    @Column(name = "category_id", nullable = false)
    private Categories categoryId;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PublicationsCategories publicationsCategories = (PublicationsCategories) o;
        return getId() != null && Objects.equals(getId(),publicationsCategories.getId()) && Objects.equals(getCategoryId(),publicationsCategories.getCategoryId())
                && Objects.equals(getPublicationId(),publicationsCategories.getPublicationId());

    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
