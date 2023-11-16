package com.hello.dbservices.services;

import com.hello.dbservices.entity.Users;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.UsersFavoritesRepository;
import com.hello.dbservices.repository.UsersRepository;
import com.hello.dbservices.response.ResponseMessage;


import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsersServices {
    private final UsersFavoritesRepository usersFavoritesRepository;
    private final UsersRepository usersRepository;


    public UsersServices(UsersFavoritesRepository usersFavoritesRepository, UsersRepository usersRepository) {
        this.usersFavoritesRepository = usersFavoritesRepository;
        this.usersRepository = usersRepository;
    }

    private ResponseMessage validation(Users user) {
        if (usersRepository.existsByEmail(user.getEmail())) {
            return new ResponseMessage("Такой email уже существует", ResponseType.UNAUTHORIZED.getCode());
        } else if (!Character.isUpperCase(user.getFirstName().charAt(0))) {
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


    public ResponseMessage addUser(Users user) {
        ResponseMessage validationResponse = validation(user);
        if (validationResponse != null) {
            return validationResponse;
        }

        usersRepository.save(user);

        return new ResponseMessage("Пользователь успешно создан", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage updateUser(Long id, Users updateUser) {
        Users user = getUsers(id);
        user.setEmail(updateUser.getEmail());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPatronym(updateUser.getPatronym());
        user.setPasswordHash(updateUser.getPasswordHash());
        user.setCreated(LocalDateTime.now());

        ResponseMessage validationResponse = validation(updateUser);
        if (validationResponse != null) {
            return validationResponse;
        }
        usersRepository.save(user);
        return new ResponseMessage("Пользователь успешно изменен", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    @Transactional
    public ResponseMessage deleteUser(Long userId) {
        usersRepository.deleteById(userId);
        return new ResponseMessage("Пользователь успешно удален", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }


}
