package com.hello.dbservices.services;

import com.hello.dbservices.entity.Categories;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.CategoriesRepository;
import com.hello.dbservices.repository.UserSessionsRepository;
import com.hello.dbservices.repository.UsersHSIRepository;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.util.UserSessionVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CategoriesServices {

    private final CategoriesRepository categoriesRepository;
    private final UserSessionsRepository userSessionsRepository;
    private final UsersHSIRepository usersHSIRepository;

    @Autowired
    public CategoriesServices(CategoriesRepository categoriesRepository, UserSessionsRepository userSessionsRepository, UsersHSIRepository usersHSIRepository) {
        this.categoriesRepository = categoriesRepository;
        this.userSessionsRepository = userSessionsRepository;
        this.usersHSIRepository = usersHSIRepository;
    }

    public Object getAll(String uuid) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else {
            return categoriesRepository.findAll();
        }
    }

    public Object addCategory(String uuid, String categoryName) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );

        if (!userSessionVerification.isSessionPresent()) {
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());
        } else {
            if (!categoriesRepository.findByNameIgnoreCase(categoryName).isEmpty()) {
                return new ResponseMessage("Категория уже существует", 409);
            } else {
                Categories category = new Categories();
                category.setName(categoryName);
                categoriesRepository.save(category);
                return new ResponseMessage("Категория создана", ResponseType.OPERATION_SUCCESSFUL.getCode());
            }
        }
    }

    @Transactional
    public ResponseMessage deleteCategories(Long categoriesId){
        categoriesRepository.deleteById(categoriesId);
        return new ResponseMessage("Категория  успешно удалена" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }
}
