package sia.plants.model;
import jakarta.persistence.*;
import java.util.*;
@Entity
@Table(name = "care_type")
public class CareType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_typeid")
    private Integer careTypeId;

    @Column(name = "name", nullable = false)
    private String name;

    public Integer getCareTypeId() {
        return careTypeId;
    }

    public void setCareTypeId(Integer careTypeId) {
        this.careTypeId = careTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}