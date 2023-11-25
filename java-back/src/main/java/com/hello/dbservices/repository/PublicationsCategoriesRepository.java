package com.hello.dbservices.repository;

import com.hello.dbservices.entity.PublicationsCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublicationsCategoriesRepository extends JpaRepository<PublicationsCategories, Long> {

    public PublicationsCategories findFirstByPublicationIdAndCategoryId(Long publicationId, Long categoryId);

}