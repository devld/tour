package me.devld.tour.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tour_spot_ticket")
public class SpotTicket extends BaseEntity {

    private Long spotId;

    private String name;

    private Integer price;

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
