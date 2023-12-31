package com.hello.util;

import com.hello.dbservices.entity.UserSessions;
import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.repository.UserSessionsRepository;
import com.hello.dbservices.repository.UsersHSIRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class UserSessionVerification {

    private final Long TIMEOUT = 600L;
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

        for (UserSessions session : userSessionsRepository.findAll()) {
            if (session.getUuid().equals(uuid)) {
                userId = session.getUserId();
                break;
            }
        }

        if (userId == null) {
            userId = 0L;
        }
        this.user = usersHSIRepository.findById(userId);
    }

    @Transactional
    public Boolean isSessionPresent() {
        if (user.isPresent()) {
            for (UserSessions session : user.get().getUserSessions()) {
                if (session.getLastUpdate().until(LocalDateTime.now(), ChronoUnit.MINUTES) > TIMEOUT)
                    userSessionsRepository.delete(session);
            }
            for (UserSessions session : user.get().getUserSessions()) {
                if (session.getUuid().equals(uuid)) {
                    if (ChronoUnit.MINUTES.between(LocalDateTime.now(), session.getLastUpdate()) < TIMEOUT || session.getIsAuthorized()) {
                        session.setLastUpdate(LocalDateTime.now());
                        userSessionsRepository.save(session);
                        return true;
                    } else {
                        userSessionsRepository.delete(session);
                    }
                }
            }
        }
        return false;
    }

    public Boolean isUserAdmin() {
        return user.map(UsersHSI::isAdmin).orElse(false);
    }

    public Boolean isUserSuper() {
        return user.map(UsersHSI::isSuperUser).orElse(false);
    }

    public Long getUserId() { return user.map(UsersHSI::getId).orElse(0L); }
}
