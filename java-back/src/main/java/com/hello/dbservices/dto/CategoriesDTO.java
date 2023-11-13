package com.hello.dbservices.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class CategoriesDTO implements Serializable {
    private Long id;
    private String name;

}
