package dbservices.services;

import dbservices.entity.Publications;
import dbservices.enums.ResponseType;
import dbservices.repository.PublicationRepository;
import dbservices.repository.PublicationTextRepository;
import dbservices.response.ResponseMessage;
import dbservices.util.PublicationValidator;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
public class PublicationServices {
    private final PublicationValidator publicationValidator;
    private final PublicationTextRepository publicationTextRepository;
    private final PublicationRepository publicationRepository;

    public PublicationServices(PublicationValidator publicationValidator, PublicationTextRepository publicationTextRepository, PublicationRepository publicationRepository) {
        this.publicationValidator = publicationValidator;
        this.publicationTextRepository = publicationTextRepository;
        this.publicationRepository = publicationRepository;
    }

    public ResponseMessage addPublication(Publications publications){
        publicationValidator.validate(publications,new BeanPropertyBindingResult(publications,"url"));
        publicationRepository.save(publications);
        return new ResponseMessage("Публикация добавлена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public String getNewsText(Long publicationId){
             String news =  publicationTextRepository.findByPublicationId(publicationId);
       return news;
    }

    public ResponseMessage deletePublication(Long publicationId){
        publicationTextRepository.deleteById(publicationId);
        publicationRepository.deleteById(publicationId);
        return new ResponseMessage("Публикация удалена", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }
}
