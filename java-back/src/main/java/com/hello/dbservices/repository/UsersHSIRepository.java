package com.hello.dbservices.repository;

import com.hello.dbservices.entity.UsersHSI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersHSIRepository extends JpaRepository<UsersHSI, Long> {

    boolean existsByEmail(String email);

}
