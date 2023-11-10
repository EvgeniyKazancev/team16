package dbservices.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "sources", schema = "test", catalog = "")
public class SourcesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
 
    @Column(name = "url")
    private String url;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "parse_depth")
    private int parseDepth;

    @Column(name = "created")
    private LocalDate created;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourcesEntity that = (SourcesEntity) o;
        return id == that.id && parseDepth == that.parseDepth && Objects.equals(url, that.url) && Objects.equals(sourceType, that.sourceType) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, sourceType, parseDepth, created);
    }
}
