package com.hello.dbservices.services;

import com.hello.dbservices.entity.Users;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.*;
import com.hello.dbservices.response.ResponseMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsersServicesTest {
    @Mock
    public UsersFavoritesRepository usersFavoritesRepository;
    @Mock
    public UsersRepository usersRepository;

    @Mock
    public UserSessionsRepository userSessionsRepository;
    @Mock
    public PublicationRepository publicationRepository;

    @Mock
    public UsersHSIRepository usersHSIRepository;

    @InjectMocks
    public UsersServices usersServices;

    @BeforeEach
    public void setUp() {
        usersServices = new UsersServices(usersFavoritesRepository, usersRepository, publicationRepository, userSessionsRepository, usersHSIRepository);
    }

    public Users getUsersTest() {
        Users users = new Users();
        users.setId(1L);
        users.setEmail("lord@yandex.ru");
        users.setFirstName("Максим");
        users.setLastName("Иванов");
        users.setPatronym("Иванович");
        users.setPasswordHash("827ccb0eea8a706c4c34a16891f84e7b");
        LocalDateTime ld = LocalDateTime.of(2023, 11, 15, 16, 15, 20);
        users.setCreated(ld);
        return users;
    }
    @Test
    public void getAllUserTest(){
        List<Users> expectedUsers = new ArrayList<>();
        expectedUsers.add(new Users());
        expectedUsers.add(new Users());

        when(usersRepository.findAll()).thenReturn(expectedUsers);
        List<Users> actualUsers = usersServices.getAllUsers();

        assertEquals(expectedUsers,actualUsers);

    }

    @Test
    public void findUserTest() {
        Users actualUser = getUsersTest();
        when(usersRepository.findById(actualUser.getId())).thenReturn(Optional.of(actualUser));
        Users expectedUser = usersServices.getUsers(actualUser.getId());
        assertEquals(actualUser, expectedUser);
    }

    @Test
    public void testAddUser() throws NoSuchAlgorithmException {
        String email = "lord@yandex.ru";
        String firstName = "John";
        String lastName = "Doe";
        String patronym = "Ivanovich";
        String passwordHash = "827ccb0eea8a706c4c34a16891f84e7b";
        LocalDateTime ld = LocalDateTime.of(2023, 11, 15, 16, 15, 20);
        Users user = new Users();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronym(patronym);
        user.setPasswordHash(passwordHash);
        user.setCreated(ld);

        when(usersRepository.save(any(Users.class))).thenReturn(user);
        ResponseMessage response = usersServices.addUser(user);

        assertEquals("Пользователь успешно создан", response.getMessage());
        assertEquals(ResponseType.OPERATION_SUCCESSFUL.getCode(), response.getCode());
    }

    @Test
    void testAddUserFirstNameNotCapitalized() throws NoSuchAlgorithmException {

        Users user = new Users();
        user.setEmail("existing@example.com");
        user.setFirstName("john");
        user.setLastName("Doe");

        ResponseMessage response = usersServices.addUser(user);

        System.out.println(response.getMessage());

        assertEquals("Имя должно начинаться с заглавной буквы", response.getMessage());
        assertEquals(ResponseType.UNAUTHORIZED.getCode(), response.getCode());
    }

    @Test
    void testAddUserLastNameNotCapitalized() throws NoSuchAlgorithmException {

        Users user = new Users();
        user.setEmail("existing@example.com");
        user.setFirstName("John");
        user.setLastName("doe");

        ResponseMessage response = usersServices.addUser(user);

        System.out.println(response.getMessage());

        assertEquals("Фамилия должна начинаться с заглавной буквы", response.getMessage());
        assertEquals(ResponseType.UNAUTHORIZED.getCode(), response.getCode());
    }


//    @Test
//    public void deleteUser() {
//        Users user = getUsersTest();
//        user.setId(1L);
//
//
//        doNothing().when(usersRepository).deleteById(user.getId());
//
//
//        ResponseMessage expected = new ResponseMessage("Пользователь успешно удален", ResponseType.OPERATION_SUCCESSFUL.getCode());
//
//        ResponseMessage actual = usersServices.deleteUser(user.getId());
//
//        assertEquals(expected.getMessage(), actual.getMessage());
//        assertEquals(expected.getCode(), actual.getCode());
//    }

}




