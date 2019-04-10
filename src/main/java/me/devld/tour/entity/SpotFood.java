package me.devld.tour.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tour_spot_food")
public class SpotFood extends BaseEntity {

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private Integer priceAverage;

    @Embedded
    private Location location;

    private String phone;

    private Integer likeCount;

    private Integer collectCount;

    @ManyToMany
    @JoinTable(
            name = "tour_spot_food_rel",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "spot_id")
    )
    private List<Spot> spots;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriceAverage() {
        return priceAverage;
    }

    public void setPriceAverage(Integer priceAverage) {
        this.priceAverage = priceAverage;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}
