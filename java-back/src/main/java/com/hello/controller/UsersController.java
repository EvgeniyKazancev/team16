package com.hello.controller;

import com.hello.dbservices.entity.Users;
import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UsersServices;
import com.hello.dbservices.services.UsersServicesHSI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UsersController {

    private final UsersServices usersServices;

    private final UsersServicesHSI usersServicesHSI;


    @Autowired
    public UsersController(UsersServices usersServices, UsersServicesHSI usersServicesHSI) {
        this.usersServices = usersServices;
        this.usersServicesHSI = usersServicesHSI;
    }

    @GetMapping("/getUserId")
    public UsersHSI getUser(@RequestParam Long userId, String uuid) {
        UsersHSI usersHSI = usersServicesHSI.getUsers(userId, uuid);
        if (usersHSI.getId() == null)
            throw new ResponseStatusException(HttpStatusCode.valueOf(403));
        return usersHSI;
    }

    @GetMapping("/getAllUsers")
    public List<UsersHSI> getAllUsers(@RequestParam String uuid) {
        List<UsersHSI> usersHSIList = usersServicesHSI.getAllUsers(uuid);
        if (usersHSIList.isEmpty())
            throw new ResponseStatusException(HttpStatusCode.valueOf(403));
        return usersHSIList;
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
