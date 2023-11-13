package dbservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "publication_data")
public class PublicationsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publications publication;

    @Column(nullable = false)
    private String property;

    @Column(nullable = false)
    private String content;

}
