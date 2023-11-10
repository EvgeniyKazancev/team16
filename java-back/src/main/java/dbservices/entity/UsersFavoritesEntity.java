package dbservices.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "users_favorites", schema = "test", catalog = "")
public class UsersFavoritesEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private UsersEntity userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "publication_id",nullable = false)
    private PublicationsEntity publicationId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersFavoritesEntity that = (UsersFavoritesEntity) o;
        return userId == that.userId && publicationId == that.publicationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, publicationId);
    }
}
