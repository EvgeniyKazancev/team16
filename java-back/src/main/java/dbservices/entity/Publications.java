package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "publications", schema = "test", catalog = "")
public class Publications {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sources_id",nullable = false)
    private Sources sourcesId;

    @Column(name = "url")
    private String url;

    @Column(name = "copies_count",columnDefinition = "DEFAULT 1")
    private int copiesCount;

    @Column(name = "hash")
    private String hash;

    @Column(name = "created")
    private LocalDateTime created;

    public Publications(){
        this.created = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publications that = (Publications) o;
        return id == that.id && copiesCount == that.copiesCount && Objects.equals(url, that.url) && Objects.equals(hash, that.hash) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, copiesCount, hash, created);
    }
}
