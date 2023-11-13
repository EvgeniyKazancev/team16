package dbservices.repository;

import dbservices.entity.PublicationsText;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationTextRepository extends JpaRepository<PublicationsText,Long> {

     PublicationsText findByPublicationId (Long publicationId);



}
