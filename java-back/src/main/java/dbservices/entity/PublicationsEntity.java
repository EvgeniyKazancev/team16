package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "publications", schema = "test", catalog = "")
public class PublicationsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "url")
    private String url;

    @Column(name = "copies_count")
    private int copiesCount;

    @Column(name = "hash")
    private String hash;

    @Column(name = "created")
    private LocalDate created;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationsEntity that = (PublicationsEntity) o;
        return id == that.id && copiesCount == that.copiesCount && Objects.equals(url, that.url) && Objects.equals(hash, that.hash) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, copiesCount, hash, created);
    }
}
