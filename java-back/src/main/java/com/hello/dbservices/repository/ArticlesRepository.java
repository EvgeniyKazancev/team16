package com.hello.dbservices.repository;

import com.hello.dbservices.entity.ArticlesEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository extends JpaRepository<ArticlesEntity,Long> {



}
