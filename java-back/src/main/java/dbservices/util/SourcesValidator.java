package dbservices.util;

import dbservices.entity.Sources;
import dbservices.enums.ResponseType;
import dbservices.repository.SourcesRepository;
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
