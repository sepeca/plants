package sia.plants.model;
import jakarta.persistence.*;
import lombok.Setter;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;

import java.sql.Timestamp;

@Entity
@Table(name = "care_history")
public class CareHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_historyid")
    private Integer careHistoryId;

    @Setter
    @Column(name = "care_date", nullable = false)
    private Timestamp careDate;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @Setter
    @Column(name = "notes")
    private String notes;

    @Setter
    @ManyToOne
    @JoinColumn(name = "plantid", nullable = false)
    private Plant plant;

    @Setter
    @ManyToOne
    @JoinColumn(name = "care_typeid", nullable = false)
    private CareType careType;

    @Setter
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Setter
    @Column(name = "user_name")
    private String userName;

    public Integer getCareHistoryId() {
        return careHistoryId;
    }



    public Timestamp getCareDate() {
        return careDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNotes() {
        return notes;
    }

    public Plant getPlant() {
        return plant;
    }

    public CareType getCareType() {
        return careType;
    }

    public User getUser() {
        return user;
    }

    public String getUserName(){
        return userName;
    }
}