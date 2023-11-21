package com.hello.dbservices.repository;

import com.hello.dbservices.entity.Categories;
import com.hello.dbservices.entity.Publications;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PublicationRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Publications> findByCreatedBetweenAndCategoriesIn(LocalDateTime startDate,
                                                                  LocalDateTime endDate,
                                                                  List<Categories> categories) {
        return entityManager.createQuery("SELECT p FROM Publications p" +
                " WHERE (p.created >= :startDate)" +
                " AND (p.created <= :endDate)" +
                " AND (p.categories = :categories)", Publications.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("categories", categories)
                .setFirstResult(15)
                .setMaxResults(5)
                .getResultList();
    }
}
