package com.hello.dbservices.dto;

import com.hello.dbservices.entity.Sources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PublicationsDTO implements Serializable {
    private Long id;
    private Sources sourcesId;
    private String url;
    private int copiesCount;
    private String hash;
    private LocalDateTime created;

}
