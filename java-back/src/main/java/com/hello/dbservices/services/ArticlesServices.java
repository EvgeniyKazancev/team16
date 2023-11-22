package com.hello.dbservices.services;

import com.hello.dbservices.entity.Articles;
import com.hello.dbservices.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticlesServices {

    private final ArticlesRepository articlesRepository;

    @Autowired
    public ArticlesServices(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    public List<Articles> getAllCaption(){
        return articlesRepository.findAll();
    }
}
