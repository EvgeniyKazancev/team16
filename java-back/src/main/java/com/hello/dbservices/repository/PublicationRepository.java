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
    Page<Publications> findPublicationsByCreatedBetweenAndPublicationsText_TextLike(LocalDateTime startDate,
                                                                                    LocalDateTime endDate,
                                                                                    String searchText,
                                                                                    Pageable pageable);

    Page<Publications> findPublicationsByCreatedBetweenAndCategoriesInAndPublicationsText_TextLike(LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       List<Categories> categories,
                                                                       String searchText,
                                                                       Pageable pageable);
    Page<Publications> findPublicationsByCreatedBetweenAndSourceIdInAndPublicationsText_TextLike(LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       List<Long> sources,
                                                                       String searchText,
                                                                       Pageable pageable);
    Page<Publications> findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdInAndPublicationsText_TextLike(LocalDateTime startDate,
                                                                   LocalDateTime endDate,
                                                                   List<Categories> categories,
                                                                   List<Long> sources,
                                                                   String searchText,
                                                                   Pageable pageable);

    List<Publications> findPublicationsByIdAndCreatedBetween(Long publicationId, LocalDateTime startDate, LocalDateTime endDate);

}
