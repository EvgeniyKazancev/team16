package com.hello.dbservices.services;

import com.hello.dbservices.entity.UserSessions;
import com.hello.dbservices.repository.UserSessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSessionServices {

    private final UserSessionsRepository userSessionsRepository;

    @Autowired
    public UserSessionServices(UserSessionsRepository userSessionsRepository) {
        this.userSessionsRepository = userSessionsRepository;
    }

    public void addUserSession(Long userId, String uuid, String token) {

        UserSessions session = new UserSessions();
        session.setUuid(uuid);
        session.setUserId(userId);
        session.setCreated(LocalDateTime.now());
        session.setLastUpdate(LocalDateTime.now());
        session.setOneTimeToken(token);
        session.setIsAuthorized(false);
        session.setTokenCreated(LocalDateTime.now());

        userSessionsRepository.save(session);
    }

    public void saveUserSession(UserSessions session) {
        session.setLastUpdate(LocalDateTime.now());
        userSessionsRepository.save(session);
    }
}
