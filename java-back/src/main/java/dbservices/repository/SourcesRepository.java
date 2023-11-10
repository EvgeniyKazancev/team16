package dbservices.repository;

import dbservices.entity.SourcesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourcesRepository extends JpaRepository<SourcesEntity,Long> {

    void deleteById(Long sourcesId);
}
