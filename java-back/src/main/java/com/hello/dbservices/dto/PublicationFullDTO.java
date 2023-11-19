package com.hello.dbservices.dto;

import com.hello.dbservices.entity.Sources;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PublicationFullDTO {
    private Long id;
    private Sources sourceId;
    private String url;
    private int copiesCount;
    private String hash;
    private LocalDateTime created;
}
