package com.hello.dbservices.services;


import com.hello.dbservices.entity.Categories;
import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.repository.*;

import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.response.ResponseMessage;


import com.hello.util.UserSessionVerification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PublicationServices {
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final PublicationTextRepository publicationTextRepository;
    private final PublicationRepository publicationRepository;
    private final UserSessionsRepository userSessionsRepository;
    private final UsersHSIRepository usersHSIRepository;

    @Autowired
    public PublicationServices(UsersRepository usersRepository,
                               CategoriesRepository categoriesRepository,
                               PublicationTextRepository publicationTextRepository,
                               PublicationRepository publicationRepository,
                               PublicationRepositoryImpl publicationRepositoryImpl,
                               SourcesRepository sourcesRepository,
                               UserSessionsRepository userSessionsRepository,
                               UsersHSIRepository usersHSIRepository) {
        this.usersRepository = usersRepository;
        this.categoriesRepository = categoriesRepository;

        this.publicationTextRepository = publicationTextRepository;
        this.publicationRepository = publicationRepository;
        this.userSessionsRepository = userSessionsRepository;
        this.usersHSIRepository = usersHSIRepository;
    }

    public Page<Publications> getPublicationsBetweenDatesInCategoriesInSources(String uuid,
                                                                                LocalDateTime startDate,
                                                                               LocalDateTime endDate,
                                                                               List<Long> catIDs,
                                                                               List<Long> sourceIDs,
                                                                               String searchText,
                                                                               Boolean favoritesOnly,
                                                                               Pageable pageable) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            return new PageImpl<>(new ArrayList<Publications>(), PageRequest.of(0,1), 0);

        if (searchText == null)
            searchText = "%";
        else
            searchText = "%" + searchText.replaceAll("\\s","%") + "%";

        Long favoriteUserId = userSessionVerification.getUserId();

        if (favoritesOnly == null)
            favoritesOnly = false;

        List<Publications> publications = new ArrayList<>();

        if (!favoritesOnly) {
            if (catIDs == null && sourceIDs == null) {
                publications = publicationRepository.findPublicationsByCreatedBetweenAndPublicationsText_TextLike(
                        startDate,
                        endDate,
                        searchText
                );
            } else if (sourceIDs == null) {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository.findPublicationsByCreatedBetweenAndCategoriesInAndPublicationsText_TextLike(
                        startDate,
                        endDate,
                        categories,
                        searchText
                );
            } else if (catIDs == null) {
                publications = publicationRepository.findPublicationsByCreatedBetweenAndSourceIdInAndPublicationsText_TextLike(
                        startDate,
                        endDate,
                        sourceIDs,
                        searchText
                );
            } else {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository.findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdInAndPublicationsText_TextLike(
                        startDate,
                        endDate,
                        categories,
                        sourceIDs,
                        searchText
                );
            }
        } else {
            if (catIDs == null && sourceIDs == null) {
                publications = publicationRepository.findPublicationsByCreatedBetweenAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_Id(
                        startDate,
                        endDate,
                        searchText,
                        favoriteUserId
                );
            } else if (sourceIDs == null) {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository.findPublicationsByCreatedBetweenAndCategoriesInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_Id(
                        startDate,
                        endDate,
                        categories,
                        searchText,
                        favoriteUserId
                );
            } else if (catIDs == null) {
                publications = publicationRepository.findPublicationsByCreatedBetweenAndSourceIdInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_Id(
                        startDate,
                        endDate,
                        sourceIDs,
                        searchText,
                        favoriteUserId
                );
            } else {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository.findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_Id(
                        startDate,
                        endDate,
                        categories,
                        sourceIDs,
                        searchText,
                        favoriteUserId
                );
            }
        }
        return new PageImpl<>(
                publications.subList(
                        pageable.getPageNumber() * pageable.getPageSize(),
                        Math.min(pageable.getPageNumber() * pageable.getPageSize() + pageable.getPageSize(), publications.size())
                ),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                ),
                publications.size());
    }

    @Transactional
    public ResponseMessage addPublication(Publications publications) {
     if(publicationRepository.existsByUrl(publications.getUrl()) ){
         return new ResponseMessage("Такой URL уже существует", ResponseType.UNAUTHORIZED.getCode());
     }
        publicationRepository.save(publications);
        return new ResponseMessage("Публикация добавлена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public String getNewsText(Long publicationId) {
        return publicationTextRepository.findByPublicationId(publicationId).getText();
    }

    public List<Publications> getAllPublicationsFromSource(Long sourcesId) {
        return publicationRepository.findAllById(sourcesId);
    }

    public List<Publications> getPublicationsFromSourceBetweenDate(Long sourcesId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Publications> publicationsList = publicationRepository.findPublicationsByIdAndCreatedBetween(sourcesId, startDate, endDate);
        if (startDate == null) {
            publicationsList = publicationRepository.findAllById(sourcesId);
        } else if
        (publicationsList.isEmpty()) {
            throw new EntityNotFoundException("Данные в указанный период отсутствуют");
        }
        return publicationsList;
    }

    @Transactional
    public ResponseMessage deletePublication(Long publicationId) {
        categoriesRepository.deleteById(publicationId);
        publicationTextRepository.deleteById(publicationId);
        publicationRepository.deleteById(publicationId);
        usersRepository.deleteById(publicationId);
        return new ResponseMessage("Публикация удалена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }
}
