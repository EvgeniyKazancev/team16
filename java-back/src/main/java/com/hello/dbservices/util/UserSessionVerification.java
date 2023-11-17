package com.hello.dbservices.util;

import com.hello.dbservices.entity.UserSession;
import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.repository.UserSessionsRepository;
import com.hello.dbservices.repository.UsersHSIRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class UserSessionVerification {

    private final int TIMEOUT = 600;
    private final String uuid;
    private final UserSessionsRepository userSessionsRepository;
    private final Optional<UsersHSI> user;

    public UserSessionVerification(
            String uuid,
            UserSessionsRepository userSessionsRepository,
            UsersHSIRepository usersHSIRepository) {
        this.uuid = uuid;
        this.userSessionsRepository = userSessionsRepository;

        Long userId = null;

        for (UserSession session : userSessionsRepository.findAll()) {
            if (session.getUuid().equals(uuid)) {
                userId = session.getUserId().getId();
            }
        }

        if (userId == null) {
            userId = 0L;
        }
        this.user = usersHSIRepository.findById(userId);
    }

    public Boolean isSessionPresent() {
        for (UserSession session : userSessionsRepository.findAll()) {
            if (session.getUuid().equals(uuid))
                if (ChronoUnit.MINUTES.between(LocalDateTime.now(), session.getLastUpdate()) < TIMEOUT) {
                    session.setLastUpdate(LocalDateTime.now());
                    return true;
                } else {
                    userSessionsRepository.delete(session);
                }
        }
        return false;
    }

    public Boolean isUserAdmin() {
        return user.isPresent() ? user.get().getIsAdmin() : false;
    }

    public Boolean isUserSuper() {
        return user.isPresent() ? user.get().getIsSuperUser() : false;
    }
}
