package me.devld.tour.dto.poi;

import me.devld.tour.entity.Location;

import java.util.List;

public class Poi {

    private String id;

    private String name;

    private List<String> type;

    private Location location;

    private List<String> phone;

    private Integer distance;

    private List<String> photos;

    private BizExt bizExt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public BizExt getBizExt() {
        return bizExt;
    }

    public void setBizExt(BizExt bizExt) {
        this.bizExt = bizExt;
    }

    public static class BizExt {
        private String rating;
        private String lowestPrice;

        public BizExt() {
        }

        public BizExt(String rating, String lowestPrice) {
            this.rating = rating;
            this.lowestPrice = lowestPrice;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(String lowestPrice) {
            this.lowestPrice = lowestPrice;
        }
    }

}
