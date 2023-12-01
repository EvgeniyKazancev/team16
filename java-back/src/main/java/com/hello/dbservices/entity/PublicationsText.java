package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "publications_text")
public class PublicationsText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "publication_id")
    private Long publicationId;

    @Column(name = "is_header")
    private boolean isHeader;

    @Column(name = "text")
    private String text;
}
