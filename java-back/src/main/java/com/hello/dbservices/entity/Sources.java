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

    @Column(name = "parse_depth", columnDefinition = "INTEGER DEFAULT 2")
    @Size(min = 1,max = 4)
    private Integer parseDepth;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "name")
    private String name;

    public Sources() {
    }
}
