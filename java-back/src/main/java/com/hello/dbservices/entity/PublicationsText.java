package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "publications_text", schema = "test")
public class PublicationsText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "publication_id")
    private Publications publicationId;

    @Column(name = "is_header")
    private boolean isHeader;

    @Column(name = "text")
    private String text;



//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null ) return false;
//        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
//        if (thisEffectiveClass != oEffectiveClass) return false;
//        PublicationsText publicationsText = (PublicationsText) o;
//
//        return getId() != null && Objects.equals(getId(),publicationsText.getId()) && Objects.equals(getPublicationId(),publicationsText.getPublicationId()) && Objects.equals(getText(),publicationsText.getText());
//    }
//
//    @Override
//    public final int hashCode() {
//        return this instanceof HibernateProxy
//                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
//                : getClass().hashCode();
//    }
}
