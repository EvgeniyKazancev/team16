package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "category_allowed_keywords", schema = "test", catalog = "")
public class CategoryAllowedKeywords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id",nullable = false)
    private Categories categoriesId;

    @Column(name = "keyword")
    private String keyword;
}
