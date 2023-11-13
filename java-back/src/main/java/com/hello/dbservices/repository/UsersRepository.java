package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

  boolean existsByEmail(String email);

}
