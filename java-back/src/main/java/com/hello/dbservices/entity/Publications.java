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

    public Publications() {
        this.created = LocalDateTime.now();
//        this.publicationsTextList = new ArrayList<>();
//        this.publicationsDataList = new ArrayList<>();
    }


//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null ) return false;
//        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
//        if (thisEffectiveClass != oEffectiveClass) return false;
//        Publications publications =(Publications) o;
//        return getId() != null && Objects.equals(getId(),publications.getId()) && Objects.equals(getUrl(), publications.getUrl()) && Objects.equals(getCopiesCount(),publications.getCopiesCount())
//                && Objects.equals(getHash(),publications.getHash()) ;
//    }
//
//    @Override
//    public final int hashCode() {
//        return this instanceof HibernateProxy
//                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
//                : getClass().hashCode();
//    }
}
