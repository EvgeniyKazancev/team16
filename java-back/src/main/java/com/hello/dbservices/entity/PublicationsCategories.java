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
    @Column(name = "id")
    private Long id;

    @Column(name = "publication_id")
    private Long publicationId;

    @Column(name = "category_id")
    private Long categoryId;
}
