package dbservices.dto;

import dbservices.entity.Publications;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
public class PublicationsTextDTO implements Serializable {
    private Long id;
    private Publications publicationId;
    private boolean isHeader;
    private String text;
}
