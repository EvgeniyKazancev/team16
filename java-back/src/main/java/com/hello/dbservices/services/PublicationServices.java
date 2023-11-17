package com.hello.dbservices.services;


import com.hello.dbservices.entity.PublicationsText;
import com.hello.dbservices.repository.UsersRepository;

import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.CategoriesRepository;
import com.hello.dbservices.repository.PublicationRepository;
import com.hello.dbservices.repository.PublicationTextRepository;
import com.hello.dbservices.response.ResponseMessage;




import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class PublicationServices {
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final PublicationTextRepository publicationTextRepository;
    private final PublicationRepository publicationRepository;

    public PublicationServices(UsersRepository usersRepository, CategoriesRepository categoriesRepository,
                               PublicationTextRepository publicationTextRepository, PublicationRepository publicationRepository) {
        this.usersRepository = usersRepository;
        this.categoriesRepository = categoriesRepository;

        this.publicationTextRepository = publicationTextRepository;
        this.publicationRepository = publicationRepository;
    }

    public ResponseMessage addPublication(Publications publications) {
       // publicationValidator.validate(publications, new BeanPropertyBindingResult(publications, "url"));
        publicationRepository.save(publications);
        return new ResponseMessage("Публикация добавлена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public String getNewsText(Long publicationId) {
        return publicationTextRepository.findByPublicationId_Id(publicationId).getText();
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

    public ResponseMessage deletePublication(Long publicationId) {
        categoriesRepository.deleteById(publicationId);
        publicationTextRepository.deleteById(publicationId);
        publicationRepository.deleteById(publicationId);
        usersRepository.deleteById(publicationId);
        return new ResponseMessage("Публикация удалена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }
}
