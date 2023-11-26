package com.hello.controller;

import com.hello.dbservices.response.ResponseMessage;
import com.hello.dbservices.services.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageDataController {

    @Autowired
    private final UserImageService userImageService;

    public ImageDataController(UserImageService userImageService) {
        this.userImageService = userImageService;
    }

    @PostMapping("/upload")
    public ResponseMessage uploadImage(String uuid, MultipartFile file) throws IOException {
        return userImageService.uploadImage(uuid, file);
    }

    @GetMapping("/download")
    public ResponseEntity<?> getImage(String uuid, @RequestParam Long userId) {

        byte[] image = userImageService.getImageByUserId(uuid, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(image);
    }
}
