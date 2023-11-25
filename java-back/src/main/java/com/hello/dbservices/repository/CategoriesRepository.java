package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories,Long> {

    List<Categories> findByIdIn(List<Long> catIDs);

    List<Categories> findByNameIgnoreCase(String name);

}
