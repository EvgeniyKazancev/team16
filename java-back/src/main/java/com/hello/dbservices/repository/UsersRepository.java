package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

  Users findFirstByEmail(String email);

  boolean existsByEmail(String email);

    Users findByUUID(String uuid);
}
