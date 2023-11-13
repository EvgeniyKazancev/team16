package dbservices.repository;

import dbservices.entity.PublicationsText;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationTextRepository extends JpaRepository<PublicationsText,Long> {

     String findByPublicationId (Long publicationId);


}
