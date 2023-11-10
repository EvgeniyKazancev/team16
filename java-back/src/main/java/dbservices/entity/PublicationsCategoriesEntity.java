package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "publications_categories", schema = "test", catalog = "")
public class PublicationsCategoriesEntity {
    @Basic
    @Column(name = "publication_id")
    private long publicationId;
    @Basic
    @Column(name = "category_id")
    private long categoryId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationsCategoriesEntity that = (PublicationsCategoriesEntity) o;
        return publicationId == that.publicationId && categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, categoryId);
    }
}
