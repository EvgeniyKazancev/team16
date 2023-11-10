package dbservices.services;

import dbservices.dto.UsersEntityDTO;
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



    public ResponseMessage addUser(UsersEntity user){

        if (userRepository.existsByEmail(user.getEmail())){
            return new ResponseMessage("Такой email уже существует", ResponseType.UNAUTHORIZED.getCode());
        }
        userRepository.save(user);
        return new ResponseMessage("Пользователь успешно создан",ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage updateUser(Long id,UsersEntity updateUser){
        UsersEntity user = getUsers(id);
        user.setEmail(updateUser.getEmail());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPatronym(updateUser.getPatronym());
        user.setPasswordHash(updateUser.getPasswordHash());
        user.setCreated(LocalDate.now());
        if (userRepository.existsByEmail(user.getEmail())){
            return new ResponseMessage("Такой email уже существует", ResponseType.UNAUTHORIZED.getCode());
        }
        userRepository.save(user);
        return new ResponseMessage("Пользователь успешно изменен", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }


    public ResponseMessage deleteUser(Long userId){
        userRepository.deleteById(userId);
        return new ResponseMessage("Пользователь успешно удален",ResponseType.OPERATION_SUCCESSFUL.getCode());
    }



}
