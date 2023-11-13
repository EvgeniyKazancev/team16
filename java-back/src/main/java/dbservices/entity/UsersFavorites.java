package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name = "users_favorites", schema = "test")
public class UsersFavorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private Users userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "publication_id",nullable = false)
    private Publications publicationId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersFavorites that = (UsersFavorites) o;
        return userId == that.userId && publicationId == that.publicationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, publicationId);
    }
}
