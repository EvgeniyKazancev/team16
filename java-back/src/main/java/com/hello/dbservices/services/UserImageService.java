package com.hello.dbservices.services;

import com.hello.dbservices.entity.UserImage;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.UserImageRepository;
import com.hello.dbservices.repository.UserSessionsRepository;
import com.hello.dbservices.repository.UsersHSIRepository;
import com.hello.dbservices.response.ResponseMessage;
import com.hello.util.UserSessionVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserImageService {

    private final UserImageRepository userImageRepository;
    private final UserSessionsRepository userSessionsRepository;
    private final UsersHSIRepository usersHSIRepository;

    @Autowired
    public UserImageService(UserImageRepository userImageRepository,
                            UserSessionsRepository userSessionsRepository,
                            UsersHSIRepository usersHSIRepository) {
        this.userImageRepository = userImageRepository;
        this.userSessionsRepository = userSessionsRepository;
        this.usersHSIRepository = usersHSIRepository;
    }

    @Transactional
    public ResponseMessage uploadImage(String uuid, MultipartFile file) throws IOException {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            return new ResponseMessage("Нет авторизации.", ResponseType.UNAUTHORIZED.getCode());

        userImageRepository.deleteByUserId(userSessionVerification.getUserId());
        userImageRepository.save(UserImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .userId(6L)
                .imageData(file.getBytes()).build());

        return new ResponseMessage("Изображение загружено", ResponseType.OPERATION_SUCCESSFUL.getCode());
    }

    @Transactional
    public byte[] getImageByUserId(String uuid, Long userId) {
        UserSessionVerification userSessionVerification = new UserSessionVerification(
                uuid,
                userSessionsRepository,
                usersHSIRepository
        );
        if (!userSessionVerification.isSessionPresent())
            return null;

        Optional<UserImage> userImage = userImageRepository.findByUserId(userId);
        byte[] image = null;
        if (userImage.isPresent()) {
            image = userImage.get().getImageData();
        }

        return image;
    }
}
