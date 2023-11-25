package com.hello.dbservices.services;

import com.hello.dbservices.entity.*;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.*;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.util.UserSessionVerification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UsersServices {
    private final UsersFavoritesRepository usersFavoritesRepository;
    private final UsersRepository usersRepository;
    private final PublicationRepository publicationRepository;
    private final UserSessionsRepository userSessionsRepository;

    private final UsersHSIRepository usersHSIRepository;

    @Autowired
    public UsersServices(
            UsersFavoritesRepository usersFavoritesRepository, UsersRepository usersRepository,
            PublicationRepository publicationRepository, UserSessionsRepository userSessionsRepository, UsersHSIRepository usersHSIRepository) {
        this.usersFavoritesRepository = usersFavoritesRepository;

        this.usersRepository = usersRepository;
        this.publicationRepository = publicationRepository;
        this.userSessionsRepository = userSessionsRepository;

        this.usersHSIRepository = usersHSIRepository;
    }

    private ResponseMessage addValidation(Users user) {
        if (usersRepository.existsByEmail(user.getEmail())) {
            return new ResponseMessage("Такой email уже существует", ResponseType.UNAUTHORIZED.getCode());
        } else if (!Character.isUpperCase(user.getFirstName().charAt(0))) {
            return new ResponseMessage("Имя должно начинаться с заглавной буквы", ResponseType.UNAUTHORIZED.getCode());

        } else if (!Character.isUpperCase(user.getLastName().charAt(0))) {
            return new ResponseMessage("Фамилия должна начинаться с заглавной буквы", ResponseType.UNAUTHORIZED.getCode());
        }
        return null;
    }

    private ResponseMessage updateValidation(Users user) {
        if (!Character.isUpperCase(user.getFirstName().charAt(0))) {
            return new ResponseMessage("Имя должно начинаться с заглавной буквы", ResponseType.UNAUTHORIZED.getCode());
        } else if (!Character.isUpperCase(user.getLastName().charAt(0))) {
            return new ResponseMessage("Фамилия должна начинаться с заглавной буквы", ResponseType.UNAUTHORIZED.getCode());
        }
        return null;
    }


    public Users getUsers(Long userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }


    public ResponseMessage addUser(Users user) throws NoSuchAlgorithmException {
        ResponseMessage validationResponse = addValidation(user);
        if (validationResponse != null) {
            return validationResponse;
        }

        usersRepository.save(user);

        return new ResponseMessage("Пользователь успешно создан", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }


    public Users getByEmail(String email) {
        return usersRepository.findFirstByEmail(email);
    }

    public UserSessions getUserSessionByUuid(String uuid) {
        return userSessionsRepository.findFirstByUuid(uuid);
    }

    public ResponseMessage updateUser(Users updateUser) {
        ResponseMessage validationResponse = updateValidation(updateUser);
        if (validationResponse != null) {
            return validationResponse;
        }
        usersRepository.save(updateUser);
        return new ResponseMessage("Пользователь успешно изменен", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }



}
