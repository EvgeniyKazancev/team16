package dbservices.services;

import dbservices.entity.Sources;
import dbservices.enums.ResponseType;
import dbservices.repository.PublicationRepository;
import dbservices.repository.SourcesRepository;
import dbservices.response.ResponseMessage;
import dbservices.util.SourcesValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.persistence.EntityNotFoundException;

@Service
public class SourcesServices {
   private final PublicationRepository publicationRepository;
    private final SourcesRepository sourcesRepository;
    private final SourcesValidator sourcesValidator;

    public SourcesServices(PublicationRepository publicationRepository, SourcesRepository sourcesRepository, SourcesValidator sourcesValidator) {
        this.publicationRepository = publicationRepository;

        this.sourcesRepository = sourcesRepository;
        this.sourcesValidator = sourcesValidator;
    }

    public Sources getSources(Long sourcesId) {
        return sourcesRepository.findById(sourcesId).orElseThrow(() -> new EntityNotFoundException("Источник не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    public ResponseMessage addSources(Sources sources) {
        sourcesValidator.validate(sources, new BeanPropertyBindingResult(sources, "url"));
        sourcesRepository.save(sources);
        return new ResponseMessage("Источник успешно добавлен" + sources.getSourceType(), ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage deleteSources(Long sourcesId) {

        sourcesRepository.deleteById(sourcesId);
        publicationRepository.deleteById(sourcesId);
        return new ResponseMessage("Источник успешно удален", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

}
