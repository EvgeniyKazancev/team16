package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Categories;
import com.hello.dbservices.entity.Sources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourcesRepository extends JpaRepository<Sources,Long> {

    boolean existsByUrl (String url);
    List<Sources> findByIdIn(List<Long> catIDs);

}
