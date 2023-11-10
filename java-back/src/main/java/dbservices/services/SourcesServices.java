package dbservices.services;

import dbservices.entity.SourcesEntity;
import dbservices.entity.UsersEntity;
import dbservices.enums.ResponseType;
import dbservices.repository.SourcesRepository;
import dbservices.response.ResponseMessage;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
public class SourcesServices {
         private final SourcesRepository sourcesRepository;

    public SourcesServices(SourcesRepository sourcesRepository) {
        this.sourcesRepository = sourcesRepository;
    }

    public SourcesEntity getSources(Long sourcesId){
        return sourcesRepository.findById(sourcesId).orElseThrow(() -> new EntityNotFoundException("Источник не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    public ResponseMessage addSources(String url,String sourcesType,int parseDepth){
        SourcesEntity sourcesEntity = new SourcesEntity();
        sourcesEntity.setUrl(url);
        sourcesEntity.setSourceType(sourcesType);
        sourcesEntity.setParseDepth(parseDepth);
        sourcesEntity.setCreated(LocalDate.now());
        sourcesRepository.save(sourcesEntity);
        return new ResponseMessage("Источник успешно добавлен" + sourcesEntity.getSourceType(), ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage deleteSources(Long sourcesId){
        sourcesRepository.deleteById(sourcesId);
        return new ResponseMessage("Источник успешно удален",ResponseType.OPERATION_SUCCESSFUL.getCode()) ;
    }

}
