package sia.plants.model.plant;
import jakarta.persistence.*;

@Entity
@Table(name = "plant_info")
public class PlantInfo {
    @Id
    @Column(name = "plant_infoid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantInfoId;

    private String humidity;
    private String lightRequirements;
    private String water;
    private String temperatureRange;


}

