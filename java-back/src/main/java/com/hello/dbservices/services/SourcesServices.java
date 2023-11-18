package com.hello.dbservices.services;

import com.hello.dbservices.entity.Sources;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.PublicationRepository;
import com.hello.dbservices.repository.SourcesRepository;
import com.hello.dbservices.response.ResponseMessage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SourcesServices {
    private final PublicationRepository publicationRepository;
    private final SourcesRepository sourcesRepository;


    public SourcesServices(PublicationRepository publicationRepository, SourcesRepository sourcesRepository) {
        this.publicationRepository = publicationRepository;

        this.sourcesRepository = sourcesRepository;

    }

    @Transactional
    public Sources getSources(Long sourcesId) {
        return sourcesRepository.findById(sourcesId).orElseThrow(() -> new EntityNotFoundException("Источник не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    @Transactional
    public ResponseMessage addSources(Sources sources) {
        if (publicationRepository.existsByUrl(sources.getUrl())) {
            return new ResponseMessage("Такой URL уже существует", ResponseType.UNAUTHORIZED.getCode());
        }
        sourcesRepository.save(sources);
        return new ResponseMessage("Источник успешно добавлен" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    @Transactional
    public ResponseMessage deleteSources(Long sourcesId) {

        sourcesRepository.deleteById(sourcesId);

        return new ResponseMessage("Источник успешно удален", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

}
