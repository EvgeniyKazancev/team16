package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Publications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Publications> findPublicationsByCreatedBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<Publications> findPublicationsByIdAndCreatedBetween(Long publicationId, LocalDateTime startDate, LocalDateTime endDate);

}
