package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "publications_categories", schema = "test", catalog = "")
public class PublicationsCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publications publicationId;
    @ManyToOne(optional = false)
    @Column(name = "category_id", nullable = false)
    private Categories categoryId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationsCategories that = (PublicationsCategories) o;
        return publicationId == that.publicationId && categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, categoryId);
    }
}
