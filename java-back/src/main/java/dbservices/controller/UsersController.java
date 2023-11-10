package dbservices.controller;

import dbservices.entity.UsersEntity;
import dbservices.response.ResponseMessage;
import dbservices.services.UsersServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UsersController {

    private final UsersServices usersServices;


    @Autowired
    public UsersController(UsersServices usersServices) {
        this.usersServices = usersServices;

    }

    @GetMapping("/getUserId")
    public UsersEntity getUser(@RequestParam Long userId){
        return usersServices.getUsers(userId);
    }

    @GetMapping("/getAllUsers")
    public List<UsersEntity> getAllUsers(){
        return usersServices.getAllUsers();
    }

    @PutMapping("/addUser")
    public ResponseMessage addUser(@RequestBody UsersEntity usersEntity){
        return usersServices.addUser(usersEntity);
    }

    @PutMapping("/updateUser")
    public ResponseMessage updateUser(@RequestParam Long id,@RequestBody UsersEntity usersEntity){
        return  usersServices.updateUser(id,usersEntity);

    }
    @DeleteMapping("/deleteUser")
    public ResponseMessage deleteUser(@RequestParam Long userId){
        return usersServices.deleteUser(userId);
    }


}
