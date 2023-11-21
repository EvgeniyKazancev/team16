package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Categories;
import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.entity.Sources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publications, Long> {

    boolean existsByUrl(String url);
    List<Publications> findAllById(Long sourcesId);
    List<Publications> findPublicationsByCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Publications> findPublicationsByCreatedBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Publications> findPublicationsByCreatedBetweenAndCategoriesIn(LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       List<Categories> categories,
                                                                       Pageable pageable);
    Page<Publications> findPublicationsByCreatedBetweenAndSourceIdIn(LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       List<Long> sources,
                                                                       Pageable pageable);
    Page<Publications> findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdIn(LocalDateTime startDate,
                                                                   LocalDateTime endDate,
                                                                   List<Categories> categories,
                                                                   List<Long> sources,
                                                                   Pageable pageable);
    List<Publications> findPublicationsByIdAndCreatedBetween(Long publicationId, LocalDateTime startDate, LocalDateTime endDate);

}
