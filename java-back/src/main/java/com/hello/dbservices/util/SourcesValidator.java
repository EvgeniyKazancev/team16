package com.hello.dbservices.util;

import com.hello.dbservices.entity.Sources;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.SourcesRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class SourcesValidator implements Validator {
    public final SourcesRepository sourcesRepository;

    public SourcesValidator(SourcesRepository sourcesRepository) {
        this.sourcesRepository = sourcesRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Sources.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
       Sources sources = (Sources) o;
       if (sourcesRepository.existsByUrl(sources.getUrl()))
           errors.rejectValue("url","" + ResponseType.UNAUTHORIZED.getCode(),"Такой URL уже существует");
    }
}
