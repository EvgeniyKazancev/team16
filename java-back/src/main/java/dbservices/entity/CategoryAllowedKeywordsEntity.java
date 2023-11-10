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
@Table(name = "category_allowed_keywords", schema = "test", catalog = "")
public class CategoryAllowedKeywordsEntity {
    @Basic
    @Column(name = "category_id")
    private long categoryId;
    @Basic
    @Column(name = "keyword")
    private String keyword;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryAllowedKeywordsEntity that = (CategoryAllowedKeywordsEntity) o;
        return categoryId == that.categoryId && Objects.equals(keyword, that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, keyword);
    }
}
