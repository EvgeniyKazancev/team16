package dbservices.repository;

import dbservices.entity.UsersFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersFavoritesRepository extends JpaRepository<UsersFavorites, Long> {

}
