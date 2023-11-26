package com.hello.dbservices.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "user_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @Column(name = "user_id")
    private Long userId;

}
