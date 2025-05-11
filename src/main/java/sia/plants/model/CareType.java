package sia.plants.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Entity
@Table(name = "care_type")
public class CareType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_typeid")
    private Integer careTypeId;

    @Setter
    @Getter
    @Column(name = "name", nullable = false)
    private String name;


}