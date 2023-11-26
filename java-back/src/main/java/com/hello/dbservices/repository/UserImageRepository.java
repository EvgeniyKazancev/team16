package com.hello.dbservices.repository;

import com.hello.dbservices.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    long deleteByUserId(Long userId);
    long countByUserId(Long userId);
    Optional<UserImage> findByUserId(Long userId);
}
