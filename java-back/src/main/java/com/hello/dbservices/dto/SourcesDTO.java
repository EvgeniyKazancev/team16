package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class SourcesDTO implements Serializable {

    private Long id;
    private String url;
    private String sourceType;
    private int parseDepth;
    private LocalDateTime created;
}
