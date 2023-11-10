package dbservices.services;

import dbservices.entity.UsersEntity;
import dbservices.enums.ResponseType;
import dbservices.repository.UserRepository;
import dbservices.response.ResponseMessage;
import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServices {
    public UsersServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;



    public UsersEntity getUsers(Long userId){
              return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден" + ResponseType.NOT_FOUND.getCode()));
    }

    public List<UsersEntity> getAllUsers(){
        return userRepository.findAll();
    }

    public ResponseMessage addUser(String email, String firstName, String lastName,
                                   String patronym, String passwordHash){
        UsersEntity user = new UsersEntity();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronym(patronym);
        user.setPasswordHash(passwordHash);
        user.setCreated(LocalDate.now());
        userRepository.save(user);
        return new ResponseMessage("Пользователь успешно создан",ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage updateUser(Long userId,String email, String firstName, String lastName,
                                      String patronym, String passwordHash){
        UsersEntity user = getUsers(userId);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronym(patronym);
        user.setPasswordHash(passwordHash);
        user.setCreated(LocalDate.now());
        userRepository.save(user);
        return new ResponseMessage("Пользователь успешно изменен", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage deleteUser(Long userId){
        userRepository.deleteById(userId);
        return new ResponseMessage("Пользователь успешно удален",ResponseType.OPERATION_SUCCESSFUL.getCode());
    }



}
