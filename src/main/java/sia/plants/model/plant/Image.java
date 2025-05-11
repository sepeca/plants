package sia.plants.model.plant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @Column(name = "imageid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Getter
    @Column(nullable = false)
    private String url;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "plantid")
    private Plant plant;


}