package dbservices.services;

import dbservices.entity.ArticlesEntity;
import dbservices.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
