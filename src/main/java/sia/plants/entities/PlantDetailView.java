package sia.plants.entities;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "plant_details_view")
@Data
public class PlantDetailView {
    @Id
    @Column(name = "plantid")
    private Integer plantId;
    @Column(name = "plantname")
    private String plantName;
    private String species;
    private String humidity;
    @Column(name = "light_requirements")
    private String lightRequirements;
    private String water;
    @Column(name = "temperature_range")
    private String temperatureRange;
    @Convert(converter = JsonListConverter.class)
    @Column(name = "imageurls")
    private List<String> imageUrls;
}
