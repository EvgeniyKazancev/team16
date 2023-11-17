package com.hello.dbservices.repository;

import com.hello.dbservices.entity.UsersHideSecureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersHideSecureInfoRepository extends JpaRepository<UsersHideSecureInfo, Long> {

    boolean existsByEmail(String email);

}
