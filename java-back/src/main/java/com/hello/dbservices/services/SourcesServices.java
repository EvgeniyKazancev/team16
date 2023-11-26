package com.hello.dbservices.services;

import com.hello.dbservices.entity.Sources;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.PublicationRepository;
import com.hello.dbservices.repository.SourcesRepository;
import com.hello.dbservices.repository.UserSessionsRepository;
import com.hello.dbservices.repository.UsersHSIRepository;
import com.hello.dbservices.response.ResponseMessage;

import com.hello.util.UserSessionVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SourcesServices {

    private final SourcesRepository sourcesRepository;
    private final UserSessionsRepository userSessionsRepository;
    private final UsersHSIRepository usersHSIRepository;

    @Autowired
    public SourcesServices(SourcesRepository sourcesRepository,
                           UserSessionsRepository userSessionsRepository,
                           UsersHSIRepository usersHSIRepository) {
        this.sourcesRepository = sourcesRepository;
        this.userSessionsRepository = userSessionsRepository;
        this.usersHSIRepository = usersHSIRepository;
    }

    public Sources getSource(String uuid, Long sourceId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Нет авторизации.");
        return sourcesRepository.findById(sourceId).orElseThrow(() -> new EntityNotFoundException("Источник не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    public Object getAllSources(String uuid) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());

        return sourcesRepository.findAll();
    }

    @Transactional
    public ResponseMessage addSource(String uuid, Sources source) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        if (!userSessionVerification.isUserAdmin() || !userSessionVerification.isUserSuper())
            return new ResponseMessage("Недостаточно прав", ResponseType.FORBIDDEN.getCode());

        if (sourcesRepository.existsByUrl(source.getUrl())) {
            return new ResponseMessage("Такой URL уже существует", 409);
        }
        sourcesRepository.save(source);
        return new ResponseMessage("Источник успешно добавлен" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    @Transactional
    public ResponseMessage deleteSource(String uuid, Long sourceId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        if (!userSessionVerification.isUserAdmin() || !userSessionVerification.isUserSuper())
            return new ResponseMessage("Недостаточно прав", ResponseType.FORBIDDEN.getCode());

        sourcesRepository.deleteById(sourceId);

        return new ResponseMessage("Источник успешно удален", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    @Transactional
    public ResponseMessage saveEditedSource(String uuid, Sources source) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        if (!userSessionVerification.isUserAdmin() || !userSessionVerification.isUserSuper())
            return new ResponseMessage("Недостаточно прав", ResponseType.FORBIDDEN.getCode());

        if (sourcesRepository.findById(source.getId()).isEmpty())
            return new ResponseMessage("Источник не найден", ResponseType.NOT_FOUND.getCode());
        sourcesRepository.save(source);
        return new ResponseMessage("Источник успешно сохранён", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

}
