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

    List<Publications> findPublicationsByCreatedBetweenAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                                    LocalDateTime endDate,
                                                                                    String searchText);

    List<Publications> findPublicationsByCreatedBetweenAndCategoriesInAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       List<Categories> categories,
                                                                       String searchText);
    List<Publications> findPublicationsByCreatedBetweenAndSourceIdInAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       List<Long> sources,
                                                                       String searchText);
    List<Publications> findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdInAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                   LocalDateTime endDate,
                                                                   List<Categories> categories,
                                                                   List<Long> sources,
                                                                   String searchText);

    List<Publications> findPublicationsByCreatedBetweenAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                                    LocalDateTime endDate,
                                                                                    String searchText,
                                                                                    Long userIdWhoFavorited);

    List<Publications> findPublicationsByCreatedBetweenAndCategoriesInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                                                   LocalDateTime endDate,
                                                                                                   List<Categories> categories,
                                                                                                   String searchText,
                                                                                                   Long userIdWhoFavorited);
    List<Publications> findPublicationsByCreatedBetweenAndSourceIdInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                                                 LocalDateTime endDate,
                                                                                                 List<Long> sources,
                                                                                                 String searchText,
                                                                                                 Long userIdWhoFavorited);
    List<Publications> findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(LocalDateTime startDate,
                                                                                                                LocalDateTime endDate,
                                                                                                                List<Categories> categories,
                                                                                                                List<Long> sources,
                                                                                                                String searchText,
                                                                                                                Long userIdWhoFavorited);

    List<Publications> findPublicationsByIdAndCreatedBetween(Long publicationId, LocalDateTime startDate, LocalDateTime endDate);

}
