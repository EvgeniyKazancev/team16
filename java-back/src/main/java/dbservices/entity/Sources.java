package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.LocalDate;
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

    @Column(name = "parse_depth",columnDefinition = "INTEGER DEFAULT 2",nullable = false)
    @Size(min = 1,max = 4)
    private int parseDepth;

    @Column(name = "created",nullable = false)
    private LocalDateTime created;

    public Sources() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sources that = (Sources) o;
        return id == that.id && parseDepth == that.parseDepth && Objects.equals(url, that.url) && Objects.equals(sourceType, that.sourceType) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, sourceType, parseDepth, created);
    }
}
