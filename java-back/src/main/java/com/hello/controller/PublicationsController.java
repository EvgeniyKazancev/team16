package com.hello.controller;

import com.hello.dbservices.entity.Publications;
import com.hello.dbservices.services.PublicationServices;
import com.hello.util.UserSessionVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pubs")
public class PublicationsController {

    private final PublicationServices publicationServices;

    public PublicationsController(PublicationServices publicationServices) {
        this.publicationServices = publicationServices;
    }

    @GetMapping("/get")
    public Page<Publications> getPublicationsBetweenDates(@RequestParam String uuid,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                          LocalDateTime startDate,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                          LocalDateTime endDate,
                                                          @RequestParam(value = "catIDs[]", required = false)
                                                          List<Long> catIDs,
                                                          @RequestParam(value = "sourceIDs[]", required = false)
                                                          List<Long> sourceIDs,
                                                          Pageable pageable) {
        return publicationServices.getPublicationsBetweenDatesInCategoriesInSources(
                uuid,
                startDate,
                endDate,
                catIDs,
                sourceIDs,
                pageable);
    }
}
