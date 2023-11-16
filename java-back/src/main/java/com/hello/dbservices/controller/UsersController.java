package com.hello.dbservices.controller;

import com.hello.dbservices.entity.Users;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UsersServices;

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
    public Users getUser(@RequestParam Long userId){
        return usersServices.getUsers(userId);
    }

    @GetMapping("/getAllUsers")
    public List<Users> getAllUsers(){
        return usersServices.getAllUsers();
    }

    @PutMapping("/addUser")
    public ResponseMessage addUser(@RequestBody Users users){

        return usersServices.addUser(users);
    }

    @PutMapping("/updateUser")
    public ResponseMessage updateUser(@RequestParam Long id,@RequestBody Users users){
        return  usersServices.updateUser(id, users);

    }
    @DeleteMapping("/deleteUser")
    public ResponseMessage deleteUser(@RequestParam Long userId){
        return usersServices.deleteUser(userId);
    }


}
