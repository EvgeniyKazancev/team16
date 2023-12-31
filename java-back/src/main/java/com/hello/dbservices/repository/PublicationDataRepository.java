package com.hello.dbservices.repository;

import com.hello.dbservices.entity.PublicationsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationDataRepository extends JpaRepository<PublicationsData, Long> {
}
