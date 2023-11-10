package dbservices.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "publications_text", schema = "test", catalog = "")
public class PublicationsTextEntity {
    @Basic
    @Column(name = "publication_id")
    private long publicationId;
    @Basic
    @Column(name = "is_header")
    private byte isHeader;
    @Basic
    @Column(name = "text")
    private String text;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationsTextEntity that = (PublicationsTextEntity) o;
        return publicationId == that.publicationId && isHeader == that.isHeader && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, isHeader, text);
    }
}
