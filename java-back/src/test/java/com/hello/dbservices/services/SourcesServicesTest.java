package com.hello.dbservices.services;

import com.hello.dbservices.entity.Sources;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.PublicationRepository;
import com.hello.dbservices.repository.SourcesRepository;

import com.hello.dbservices.response.ResponseMessage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SourcesServicesTest {

    @Mock
    public PublicationRepository publicationRepository;

    @Mock
    public SourcesRepository sourcesRepository;

    @InjectMocks
    public SourcesServices sourcesServices;

    @BeforeEach
    public void setSourcesServices(){
        sourcesServices = new SourcesServices(publicationRepository,sourcesRepository);
    }

    public Sources getSourcesTest(){
        Sources sources = new Sources();
        sources.setId(2L);
        sources.setSourceType("Telegram");
        LocalDateTime ld = LocalDateTime.of(2023,11,11,10,15,28);


        return sources;
    }
    @Test
    public void findSourcesTest(){
        Sources actualSources = getSourcesTest();

        Mockito.when(sourcesRepository.findById(actualSources.getId())).thenReturn(Optional.of(actualSources));

        Sources expectedSources = sourcesServices.getSources(actualSources.getId());
      //  System.out.println(expectedSources.getSourceType());
        assertEquals(expectedSources,actualSources);

    }


}