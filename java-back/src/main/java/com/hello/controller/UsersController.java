package com.hello.dbservices.controller;

import com.hello.dbservices.entity.Users;
<<<<<<< HEAD:java-back/src/main/java/com/hello/dbservices/controller/UsersController.java
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UsersServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
=======
import com.hello.dbservices.entity.UsersHSI;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UsersServices;
import com.hello.dbservices.services.UsersServicesHSI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
>>>>>>> 626ae04 (User and login API in progress.):java-back/src/main/java/com/hello/controller/UsersController.java
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UsersController {

    private final UsersServices usersServices;

    private final UsersServicesHSI usersServicesHSI;


    @Autowired
    public UsersController(UsersServices usersServices, UsersServicesHSI usersServicesHSI) {
        this.usersServices = usersServices;
<<<<<<< HEAD:java-back/src/main/java/com/hello/dbservices/controller/UsersController.java

    }

    @GetMapping("/getUserId")
    public Users getUser(@RequestParam Long userId){
        return usersServices.getUsers(userId);
    }

    @GetMapping("/getAllUsers")
    public List<Users> getAllUsers(){
        return usersServices.getAllUsers();
=======
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
>>>>>>> 626ae04 (User and login API in progress.):java-back/src/main/java/com/hello/controller/UsersController.java
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
