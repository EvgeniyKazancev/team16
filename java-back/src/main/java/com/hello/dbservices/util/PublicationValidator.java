package com.hello.dbservices.util;

import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.PublicationRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PublicationValidator implements Validator {
private final PublicationRepository publicationRepository;

    public PublicationValidator(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return Publications.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Publications publications = (Publications) o;
       if (publicationRepository.existsByUrl(publications.getUrl()) ){
           errors.rejectValue("url","" + ResponseType.UNAUTHORIZED.getCode(), "Такой URL уже существует" );
       }
    }
}
