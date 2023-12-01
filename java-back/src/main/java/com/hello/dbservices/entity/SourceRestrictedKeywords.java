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
}
