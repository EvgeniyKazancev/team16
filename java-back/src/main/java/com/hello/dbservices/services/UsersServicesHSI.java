package com.hello.dbservices.services;

import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.UserSessionsRepository;
import com.hello.dbservices.repository.UsersHSIRepository;
import com.hello.dbservices.repository.UsersRepository;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.util.UserSessionVerification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServicesHSI {

    private final UsersHSIRepository usersHSIRepository;
    private final UserSessionsRepository userSessionsRepository;

    @Autowired
    public UsersServicesHSI(
            UsersHSIRepository usersHSIRepository,
            UserSessionsRepository userSessionsRepository
    ) {
        this.usersHSIRepository = usersHSIRepository;
        this.userSessionsRepository = userSessionsRepository;
    }

    public UsersHSI getUser(Long userId, String uuid) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        return userSessionVerification.isSessionPresent() ? usersHSIRepository.
                findById(userId).
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден" + ResponseType.NOT_FOUND.getCode()))
                :
                new UsersHSI();
    }

    public List<UsersHSI> getAllUsers(String uuid) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        return userSessionVerification.isSessionPresent() ? usersHSIRepository.findAll() : new ArrayList<>();
    }

    public ResponseMessage addUser(UsersHSI user, String uuid) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else if (!userSessionVerification.isUserSuper()) {
            return new ResponseMessage("Недостаточно прав.", ResponseType.FORBIDDEN.getCode());
        } else {
            usersHSIRepository.save(user);
            return new ResponseMessage("Пользователь успешно создан.", ResponseType.OPERATION_SUCCESSFUL.getCode());
        }

    }

}
