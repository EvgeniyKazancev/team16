package dbservices.services;

import dbservices.entity.CategoriesEntity;
import dbservices.enums.ResponseType;
import dbservices.repository.CategoriesRepository;
import dbservices.response.ResponseMessage;
import org.springframework.stereotype.Service;

@Service
public class CategoriesServices {
    private final CategoriesRepository categoriesRepository;

    public CategoriesServices(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public  ResponseMessage addCategories(String name){
        CategoriesEntity categoriesEntity = new CategoriesEntity();
        categoriesEntity.setName(name);
        categoriesRepository.save(categoriesEntity);
        return new ResponseMessage("Категория  успешно создана" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    public ResponseMessage deleteCategories(Long categoriesId){
        categoriesRepository.deleteById(categoriesId);
        return new ResponseMessage("Категория  успешно удалена" , ResponseType.OPERATION_SUCCESSFUL.getCode());
    }
}
