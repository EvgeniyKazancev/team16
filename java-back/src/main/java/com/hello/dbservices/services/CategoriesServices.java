package com.hello.dbservices.services;

import com.hello.dbservices.entity.Categories;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.CategoriesRepository;
import com.hello.dbservices.response.ResponseMessage;
import org.springframework.stereotype.Service;

@Service
public class CategoriesServices {
    private final CategoriesRepository categoriesRepository;

    public CategoriesServices(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public  ResponseMessage addCategories(Categories categories){

        categoriesRepository.save(categories);
        return new ResponseMessage("Категория  успешно создана" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage deleteCategories(Long categoriesId){
        categoriesRepository.deleteById(categoriesId);
        return new ResponseMessage("Категория  успешно удалена" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }
}