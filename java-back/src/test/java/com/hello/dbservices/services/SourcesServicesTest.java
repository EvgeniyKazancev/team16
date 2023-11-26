package com.hello.dbservices.services;

import com.hello.dbservices.entity.Sources;
import com.hello.dbservices.enums.ResponseType;
import com.hello.dbservices.repository.PublicationRepository;
import com.hello.dbservices.repository.SourcesRepository;

import com.hello.dbservices.response.ResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void setUp(){
        sourcesServices = new SourcesServices(publicationRepository,sourcesRepository, userSessionsRepository, usersHSIRepository);
    }

    public Sources getSourcesTest(){
        Sources sources = new Sources();
        sources.setId(2L);
        sources.setUrl("https://www.youtube.com");
        sources.setSourceType("Telegram");
        LocalDateTime ld = LocalDateTime.of(2023,11,11,10,15,28);


        return sources;
    }
    @Test
    public void findSourcesTest(){
        Sources actualSources = getSourcesTest();

        Mockito.when(sourcesRepository.findById(actualSources.getId())).thenReturn(Optional.of(actualSources));

        Sources expectedSources = sourcesServices.getSource(actualSources.getId());
      //  System.out.println(expectedSources.getSourceType());
        assertEquals(expectedSources,actualSources);

    }

    @Test
    public void addSourcesTest(){
        Sources sources = new Sources();
        sources.setId(1L);
        sources.setUrl("https://www.youtube.com");
        sources.setSourceType("Telegram");
        LocalDateTime ld = LocalDateTime.of(2023,11,11,10,15,28);
        sources.setCreated(ld);

        when(sourcesRepository.save(any(Sources.class))).thenReturn(sources);


        ResponseMessage response = sourcesServices.addSource(sources);

        assertEquals("Источник успешно добавлен", response.getMessage());
        assertEquals(ResponseType.OPERATION_SUCCESSFUL.getCode(), response.getCode());
    }

    @Test
    public void addSourcesUrlNotCapitalized(){
        String url = "https://www.youtube.com";
        Sources sources = new Sources();
        sources.setUrl(url);

        when(publicationRepository.existsByUrl(url)).thenReturn(true);
        ResponseMessage responseMessage = sourcesServices.addSource(sources);

        assertEquals("Такой URL уже существует",responseMessage.getMessage());
        assertEquals(ResponseType.UNAUTHORIZED.getCode(),responseMessage.getCode());
    }
    @Test
    public void deleteSourcesTest(){
        Sources sources = new Sources();
        sources.setId(1L);

       doNothing().when(sourcesRepository).deleteById(sources.getId());
       ResponseMessage responseMessage = sourcesServices.deleteSource(sources.getId());

       assertEquals("Источник успешно удален",responseMessage.getMessage());
       assertEquals(ResponseType.OPERATION_SUCCESSFUL.getCode(),responseMessage.getCode());
    }

}

















































