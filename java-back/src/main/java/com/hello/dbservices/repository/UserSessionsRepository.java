package com.hello.dbservices.repository;

import com.hello.dbservices.entity.UserSessions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionsRepository extends CrudRepository<UserSessions, Long> {

    UserSessions findFirstByUuid(String uuid);

}
