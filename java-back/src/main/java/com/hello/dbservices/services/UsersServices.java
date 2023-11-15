package com.hello.dbservices.services;

import com.hello.dbservices.entity.Users;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.UsersFavoritesRepository;
import com.hello.dbservices.repository.UsersRepository;
import com.hello.dbservices.repository.UsersHideSecureInfoRepository;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.util.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsersServices {
    private final UsersFavoritesRepository usersFavoritesRepository;
    private final UsersRepository usersRepository;
    private final UsersHideSecureInfoRepository usersHideSecureInfoRepository;
    private final UserValidator userValidator;

    public UsersServices(UsersFavoritesRepository usersFavoritesRepository, UsersRepository usersRepository, UsersHideSecureInfoRepository usersHideSecureInfoRepository, UserValidator userValidator) {
        this.usersFavoritesRepository = usersFavoritesRepository;
        this.usersRepository = usersRepository;
        this.usersHideSecureInfoRepository = usersHideSecureInfoRepository;
        this.userValidator = userValidator;
    }


    public Users getUsers(Long userId) {
        return usersRepository.
                findById(userId).
                orElseThrow(() -> new EntityNotFoundException("Пользователь не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }


    public ResponseMessage addUser(Users user) {

        userValidator.validate(user, new BeanPropertyBindingResult(user, "email"));
        userValidator.validate(user, new BeanPropertyBindingResult(user, "firstName"));
        userValidator.validate(user, new BeanPropertyBindingResult(user, "lastName"));
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return new ResponseMessage("Такой email уже существует", ResponseType.UNAUTHORIZED.getCode());
//        }
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
        userValidator.validate(user, new BeanPropertyBindingResult(user, "email"));
        userValidator.validate(user, new BeanPropertyBindingResult(user, "firstName"));
        userValidator.validate(user, new BeanPropertyBindingResult(user, "lastName"));
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return new ResponseMessage("Такой email уже существует", ResponseType.UNAUTHORIZED.getCode());
//        }
        usersRepository.save(user);
        return new ResponseMessage("Пользователь успешно изменен", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }


    public ResponseMessage deleteUser(Long userId) {
        usersFavoritesRepository.deleteById(userId);
        usersRepository.deleteById(userId);
        return new ResponseMessage("Пользователь успешно удален", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }


}
