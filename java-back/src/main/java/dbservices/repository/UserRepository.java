package dbservices.repository;

import dbservices.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity,Long> {

  boolean existsByEmail(String email);
   @Override
   void deleteById(Long userId);
}
