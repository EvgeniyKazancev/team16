package dbservices.repository;

import dbservices.entity.Publications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends JpaRepository<Publications, Long> {

    boolean existsByUrl(String url);

  //  void deleteById(Long publicationId);
}
