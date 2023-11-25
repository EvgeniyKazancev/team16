package com.hello.dbservices.services;


import com.hello.dbservices.entity.*;
import com.hello.dbservices.repository.*;

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
import java.util.List;
import java.util.Optional;

@Service
public class PublicationServices {
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final PublicationTextRepository publicationTextRepository;
    private final PublicationRepository publicationRepository;
    private final UserSessionsRepository userSessionsRepository;
    private final UsersHSIRepository usersHSIRepository;
    private final UsersFavoritesRepository usersFavoritesRepository;
    private final PublicationsCategoriesRepository publicationsCategoriesRepository;

    @Autowired
    public PublicationServices(UsersRepository usersRepository,
                               CategoriesRepository categoriesRepository,
                               PublicationTextRepository publicationTextRepository,
                               PublicationRepository publicationRepository,
                               PublicationRepositoryImpl publicationRepositoryImpl,
                               SourcesRepository sourcesRepository,
                               UserSessionsRepository userSessionsRepository,
                               UsersHSIRepository usersHSIRepository,
                               UsersFavoritesRepository usersFavoritesRepository,
                               PublicationsCategoriesRepository publicationsCategoriesRepository) {
        this.usersRepository = usersRepository;
        this.categoriesRepository = categoriesRepository;

        this.publicationTextRepository = publicationTextRepository;
        this.publicationRepository = publicationRepository;
        this.userSessionsRepository = userSessionsRepository;
        this.usersHSIRepository = usersHSIRepository;
        this.usersFavoritesRepository = usersFavoritesRepository;
        this.publicationsCategoriesRepository = publicationsCategoriesRepository;
    }

    public Object getPublicationsBetweenDatesInCategoriesInSources(String uuid,
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
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());

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
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(
                        startDate,
                        endDate,
                        searchText
                );
            } else if (sourceIDs == null) {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndCategoriesInAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(
                        startDate,
                        endDate,
                        categories,
                        searchText
                );
            } else if (catIDs == null) {
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndSourceIdInAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(
                        startDate,
                        endDate,
                        sourceIDs,
                        searchText
                );
            } else {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdInAndPublicationsText_TextLikeAndRemovedFalseOrderByCreatedDesc(
                        startDate,
                        endDate,
                        categories,
                        sourceIDs,
                        searchText
                );
            }
        } else {
            if (catIDs == null && sourceIDs == null) {
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(
                        startDate,
                        endDate,
                        searchText,
                        favoriteUserId
                );
            } else if (sourceIDs == null) {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndCategoriesInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(
                        startDate,
                        endDate,
                        categories,
                        searchText,
                        favoriteUserId
                );
            } else if (catIDs == null) {
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndSourceIdInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(
                        startDate,
                        endDate,
                        sourceIDs,
                        searchText,
                        favoriteUserId
                );
            } else {
                List<Categories> categories = new ArrayList<>(categoriesRepository.findByIdIn(catIDs));
                publications = publicationRepository
                        .findPublicationsByCreatedBetweenAndCategoriesInAndSourceIdInAndPublicationsText_TextLikeAndUsersWhoHaveFavorited_IdAndRemovedFalseOrderByCreatedDesc(
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

    public ResponseMessage addUserFavoritesPublication(String uuid, Long publicationId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else {
            Users users = usersRepository.findByUserSessions_Uuid(uuid);
            UsersFavorites usersFavorites = new UsersFavorites();
            usersFavorites.setUserId(users.getId());
            usersFavorites.setPublicationId(publicationRepository.getReferenceById(publicationId));
            usersFavoritesRepository.save(usersFavorites);

            return new ResponseMessage("Публикация добавлена в избранное", ResponseType.OPERATION_SUCCESSFUL.getCode());
        }
    }
    public ResponseMessage removeUserFavoritesPublication(String uuid, Long publicationId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else {
            UsersFavorites usersFavorite = usersFavoritesRepository.
                    findFirstByPublicationId(publicationRepository.getReferenceById(publicationId));
            if (usersFavorite != null) {
                usersFavoritesRepository.delete(usersFavorite);
                return new ResponseMessage("Публикация удалена из избранного", ResponseType.OPERATION_SUCCESSFUL.getCode());
            } else {
                return new ResponseMessage("Публикация не найдена", ResponseType.NOT_FOUND.getCode());
            }
        }
    }

    public ResponseMessage addPublicationCategory(String uuid, Long publicationId, Long categoryId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else {
            PublicationsCategories publicationsCategory = new PublicationsCategories();
            publicationsCategory.setCategoryId(categoryId);
            publicationsCategory.setPublicationId(publicationId);
            publicationsCategoriesRepository.save(publicationsCategory);
            return new ResponseMessage("Категория добавлена к публикации", ResponseType.OPERATION_SUCCESSFUL.getCode());
        }
    }

    public ResponseMessage removePublicationCategory(String uuid, Long publicationId, Long categoryId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else {
            PublicationsCategories publicationsCategory = publicationsCategoriesRepository
                    .findFirstByPublicationIdAndCategoryId(publicationId, categoryId);
            if (publicationsCategory != null) {
                publicationsCategoriesRepository.delete(publicationsCategory);
                return new ResponseMessage("Категория удалена из публикации", ResponseType.OPERATION_SUCCESSFUL.getCode());
            } else {
                return new ResponseMessage("Не найдено соответствия категории и публикации",
                        ResponseType.NOT_FOUND.getCode());
            }
        }
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
    public ResponseMessage deletePublication(String uuid, Long publicationId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else {
            Optional<Publications> publication = publicationRepository.findById(publicationId);
            if (publication.isPresent()) {
                publication.get().setRemoved(true);
                publicationRepository.save(publication.get());
                return new ResponseMessage("Публикация удалена", ResponseType.OPERATION_SUCCESSFUL.getCode());
            } else {
                return new ResponseMessage("Публикация не найдена", ResponseType.NOT_FOUND.getCode());
            }
        }
    }
}
