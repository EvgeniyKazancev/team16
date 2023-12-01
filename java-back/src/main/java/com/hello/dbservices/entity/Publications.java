package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publications implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "copies_count", columnDefinition = "DEFAULT 1")
    private int copiesCount;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "removed",columnDefinition = "TINYINT(1)",nullable = false,length = 1)
    private boolean removed;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Sources source;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="publication_id")
    private List<PublicationsText> publicationsText;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="publication_id")
    private List<PublicationsData> publicationsData;

    @ManyToMany
    @JoinTable(
            name = "publications_categories",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Categories> categories;

    @ManyToMany
    @JoinTable(
            name = "users_favorites",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UsersHSISessionsOff> usersWhoHaveFavorited;

    public Publications() {
        this.created = LocalDateTime.now();
    }
}
