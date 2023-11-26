package com.hello.controller;

import com.hello.dbservices.entity.Sources;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.SourcesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sources")
public class SourcesController {

    @Autowired
    private final SourcesServices sourcesServices;

    public SourcesController(SourcesServices sourcesServices) {
        this.sourcesServices = sourcesServices;
    }

    @PostMapping("/add")
    public ResponseMessage addSource(String uuid, Sources source) {
        return sourcesServices.addSource(uuid, source);
    }

    @PostMapping("/delete")
    public ResponseMessage deleteSource(String uuid, Long sourceId) {
        return sourcesServices.deleteSource(uuid, sourceId);
    }

    @PostMapping("/saveEdited")
    public ResponseMessage saveEditedSource(String uuid, Sources source) {
        return sourcesServices.saveEditedSource(uuid, source);
    }

    @GetMapping("/getById")
    public Sources getSourceById(String uuid, Long sourceId) {
        return sourcesServices.getSource(uuid, sourceId);
    }

    @GetMapping("/getAll")
    public Object getAllSources(String uuid) {
        return sourcesServices.getAllSources(uuid);
    }
}
