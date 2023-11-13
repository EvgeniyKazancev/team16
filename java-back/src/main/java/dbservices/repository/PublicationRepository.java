package dbservices.repository;

import dbservices.entity.Publications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publications, Long> {

    boolean existsByUrl(String url);
       List<Publications> findAllById(Long sourcesId);
    List<Publications> findPublicationsByIdAndCreatedBetween(Long publicationId, LocalDateTime startDate, LocalDateTime endDate);

  //  void deleteById(Long publicationId);
}