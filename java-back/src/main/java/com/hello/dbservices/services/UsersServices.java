package com.hello.dbservices.services;

import com.hello.dbservices.entity.UserSessions;
import com.hello.dbservices.entity.Users;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.UserSessionsRepository;
import com.hello.dbservices.repository.UsersFavoritesRepository;
import com.hello.dbservices.repository.UsersRepository;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.util.MD5Calculation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UsersServices {
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final UserSessionsRepository userSessionsRepository;

    public UsersServices(UsersFavoritesRepository usersFavoritesRepository,
                         UsersRepository usersRepository,
                         UserSessionsRepository userSessionsRepository) {
        this.usersRepository = usersRepository;
        this.userSessionsRepository = userSessionsRepository;
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


    @Transactional
    public Users getUsers(Long userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    @Transactional
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Transactional
    public ResponseMessage addUser(Users user) throws NoSuchAlgorithmException {
        ResponseMessage validationResponse = addValidation(user);
        if (validationResponse != null) {
            return validationResponse;
        }

        usersRepository.save(user);

        return new ResponseMessage("Пользователь успешно создан", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    @Transactional
    public Users getByEmail(String email) {
        return usersRepository.findFirstByEmail(email);
    }

    @Transactional
    public UserSessions getUserSessionByUuid(String uuid) {
        return userSessionsRepository.findFirstByUuid(uuid);
    }

    @Transactional
    public ResponseMessage updateUser(Users updateUser) {
        ResponseMessage validationResponse = updateValidation(updateUser);
        if (validationResponse != null) {
            return validationResponse;
        }
        usersRepository.save(updateUser);
        return new ResponseMessage("Пользователь успешно изменен", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

}
