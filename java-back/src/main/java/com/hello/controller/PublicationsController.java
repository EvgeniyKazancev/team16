package com.hello.controller;

import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.PublicationServices;
import com.hello.dbservices.services.CategoriesServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pubs")
public class PublicationsController {

    private final PublicationServices publicationServices;
    private final CategoriesServices categoriesServices;

    public PublicationsController(PublicationServices publicationServices, CategoriesServices categoriesServices) {
        this.publicationServices = publicationServices;
        this.categoriesServices = categoriesServices;
    }

    @GetMapping(value = "/get")
    public Object getPublicationsBetweenDates(@RequestParam String uuid,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                          LocalDateTime startDate,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                          LocalDateTime endDate,
                                                          @RequestParam(value = "catIDs[]", required = false)
                                                          List<Long> catIDs,
                                                          @RequestParam(value = "sourceIDs[]", required = false)
                                                          List<Long> sourceIDs,
                                                          @RequestParam(value = "searchString", required = false)
                                                          String searchString,
                                                          @RequestParam(value = "favoritesOnly", required = false)
                                                          Boolean favoritesOnly,
                                                          Pageable pageable) {
        return publicationServices.getPublicationsBetweenDatesInCategoriesInSources(
                uuid,
                startDate,
                endDate,
                catIDs,
                sourceIDs,
                searchString,
                favoritesOnly,
                pageable);
    }

    @PostMapping("/removeFavoritesPublication")
    public ResponseMessage removeFavoritesPublications(String uuid, Long publicationId){
        return publicationServices.removeUserFavoritesPublication(uuid, publicationId);
    }

    @PostMapping("/addFavoritesPublication")
    public ResponseMessage addFavoritesPublications(String uuid, Long publicationId){
        return publicationServices.addUserFavoritesPublication(uuid, publicationId);
    }

    @PostMapping("/addPublicationCategory")
    public ResponseMessage addPublicationCategory(String uuid, Long publicationId, Long categoryId) {
        return publicationServices.addPublicationCategory(uuid, publicationId, categoryId);
    }

    @PostMapping("/removePublicationCategory")
    public ResponseMessage removePublicationCategory(String uuid, Long publicationID, Long categoryId) {
        return publicationServices.removePublicationCategory(uuid, publicationID, categoryId);
    }

    @PostMapping("removeById")
    public ResponseMessage removePublication(String uuid, Long publicationId) {
        return publicationServices.deletePublication(uuid, publicationId);
    }
}
