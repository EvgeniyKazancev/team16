package dbservices.controller;

import dbservices.entity.UsersEntity;
import dbservices.response.ResponseMessage;
import dbservices.services.UsersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public ResponseMessage addUser(@RequestParam String email,@RequestParam String firstName,@RequestParam String lastName,
                                   @RequestParam String patronum,@RequestParam String passwordHash){
        return usersServices.addUser(email,firstName,lastName,patronum,passwordHash);
    }

    @PutMapping("/updateUser")
    public ResponseMessage updateUser(@RequestParam Long userId,@RequestParam String email,@RequestParam String firstName,@RequestParam String lastName,
                                      @RequestParam String patronum,@RequestParam String passwordHash){
        return  usersServices.updateUser(userId,email,firstName,lastName,patronum,passwordHash);

    }
    @DeleteMapping("/deleteUser")
    public ResponseMessage deleteUser(Long userId){
        return usersServices.deleteUser(userId);
    }


}
