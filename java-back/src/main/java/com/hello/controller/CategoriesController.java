package com.hello.controller;

import com.hello.dbservices.entity.Categories;
import com.hello.dbservices.repository.CategoriesRepository;
import com.hello.dbservices.services.CategoriesServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesServices categoriesServices;

    public CategoriesController(CategoriesServices categoriesServices) {
        this.categoriesServices = categoriesServices;
    }

    @GetMapping("/getAll")
    public Object getAll(@RequestParam String uuid) {
        return categoriesServices.getAll(uuid);
    }

    @PostMapping("/addCategory")
    public Object addCategory(String uuid, String categoryName) {
        return categoriesServices.addCategory(uuid, categoryName);
    }
}
