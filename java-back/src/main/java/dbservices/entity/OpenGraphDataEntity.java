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
@Table(name = "open_graph_data", schema = "test", catalog = "")
public class OpenGraphDataEntity {
    @Basic
    @Column(name = "publication_id")
    private long publicationId;
    @Basic
    @Column(name = "property")
    private String property;
    @Basic
    @Column(name = "content")
    private String content;

    public long getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(long publicationId) {
        this.publicationId = publicationId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenGraphDataEntity that = (OpenGraphDataEntity) o;
        return publicationId == that.publicationId && Objects.equals(property, that.property) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, property, content);
    }
}
