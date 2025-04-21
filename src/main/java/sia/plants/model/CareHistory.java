package sia.plants.model;
import jakarta.persistence.*;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;

import java.sql.Timestamp;
import java.util.*;
@Entity
@Table(name = "care_history")
public class CareHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_historyid")
    private Integer careHistoryId;

    @Column(name = "care_date", nullable = false)
    private Timestamp careDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "plantid", nullable = false)
    private Plant plant;

    @ManyToOne
    @JoinColumn(name = "care_typeid", nullable = false)
    private CareType careType;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    public Integer getCareHistoryId() {
        return careHistoryId;
    }

    public void setCareHistoryId(Integer careHistoryId) {
        this.careHistoryId = careHistoryId;
    }

    public Timestamp getCareDate() {
        return careDate;
    }

    public void setCareDate(Timestamp careDate) {
        this.careDate = careDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public CareType getCareType() {
        return careType;
    }

    public void setCareType(CareType careType) {
        this.careType = careType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}