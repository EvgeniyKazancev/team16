package com.hello.dbservices.services;

<<<<<<< HEAD:java-back/src/main/java/dbservices/services/PublicationServices.java
import dbservices.entity.Publications;
import dbservices.entity.PublicationsText;
import dbservices.enums.ResponseType;
import dbservices.repository.CategoriesRepository;
import dbservices.repository.PublicationRepository;
import dbservices.repository.PublicationTextRepository;
import dbservices.repository.UsersRepository;
import dbservices.response.ResponseMessage;
import dbservices.util.PublicationValidator;
=======
import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.CategoriesRepository;
import com.hello.dbservices.repository.PublicationRepository;
import com.hello.dbservices.repository.PublicationTextRepository;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.util.PublicationValidator;
>>>>>>> e57ede8 (JPA pre-edit.):java-back/src/main/java/com/hello/dbservices/services/PublicationServices.java

import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicationServices {
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final PublicationValidator publicationValidator;
    private final PublicationTextRepository publicationTextRepository;
    private final PublicationRepository publicationRepository;

    public PublicationServices(UsersRepository usersRepository, CategoriesRepository categoriesRepository, PublicationValidator publicationValidator,
                               PublicationTextRepository publicationTextRepository, PublicationRepository publicationRepository) {
        this.usersRepository = usersRepository;
        this.categoriesRepository = categoriesRepository;
        this.publicationValidator = publicationValidator;
        this.publicationTextRepository = publicationTextRepository;
        this.publicationRepository = publicationRepository;
    }

    public ResponseMessage addPublication(Publications publications){
        publicationValidator.validate(publications,new BeanPropertyBindingResult(publications,"url"));
        publicationRepository.save(publications);
        return new ResponseMessage("Публикация добавлена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public PublicationsText getNewsText(Long publicationId){
             PublicationsText news =  publicationTextRepository.findByPublicationId(publicationId);
       return news;
    }

    public List<Publications> getAllPublicationsFromSource(Long sourcesId){
        return publicationRepository.findAllById(sourcesId);
    }
    public List<Publications> getPublicationsFromSourceBetweenDate(Long sourcesId, LocalDateTime startDate, LocalDateTime endDate){
      List<Publications> publicationsList = publicationRepository.findPublicationsByIdAndCreatedBetween(sourcesId,startDate,endDate);
        if(startDate == null){
           publicationsList = publicationRepository.findAllById(sourcesId);
        }else if
        (publicationsList.isEmpty()){
            throw new EntityNotFoundException("Данные в указанный период отсутствуют");
        }
           return publicationsList;
    }

    public ResponseMessage deletePublication(Long publicationId){
        categoriesRepository.deleteById(publicationId);
        publicationTextRepository.deleteById(publicationId);
        publicationRepository.deleteById(publicationId);
        usersRepository.deleteById(publicationId);
        return new ResponseMessage("Публикация удалена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }
}
