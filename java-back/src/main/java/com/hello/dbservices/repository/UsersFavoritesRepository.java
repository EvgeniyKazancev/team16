package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.entity.UsersFavorites;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersFavoritesRepository extends CrudRepository<UsersFavorites, Long> {

    UsersFavorites findFirstByPublicationId(Publications publication);

}
