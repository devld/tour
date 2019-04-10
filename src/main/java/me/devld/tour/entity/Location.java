package me.devld.tour.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Location {

    /**
     * 人可读的位置
     */
    private String location;

    /**
     * 位置 id, 对应于 {@link District} 的 id
     */
    @Column(columnDefinition = "smallint(5)")
    private Integer locationId;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
