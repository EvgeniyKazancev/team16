package com.hello.dbservices.services;

import com.hello.dbservices.entity.ArticlesEntity;
import com.hello.dbservices.repository.ArticlesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticlesServices {

    private final ArticlesRepository articlesRepository;


    public ArticlesServices(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    public List<ArticlesEntity> getAllCaption(){
        return articlesRepository.findAll();
    }
}
