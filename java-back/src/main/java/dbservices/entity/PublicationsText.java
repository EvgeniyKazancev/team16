package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "publications_text", schema = "test")
public class PublicationsText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "publication_id")
    private Publications publicationId;

    @Column(name = "is_header")
    private boolean isHeader;

    @Column(name = "text")
    private String text;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationsText that = (PublicationsText) o;
        return publicationId == that.publicationId && isHeader == that.isHeader && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, isHeader, text);
    }
}
