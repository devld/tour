package me.devld.tour.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tour_spot_photo")
public class SpotPhoto extends BaseStateEntity {

    private String imgUrl;

    private String description;

    private Long spotId;

    private Long fromId;

    @Column(columnDefinition = "tinyint")
    private PhotoFrom photoFrom;

    private Long authorId;

    private Integer likeCount = 0;

    public SpotPhoto() {
    }

    public SpotPhoto(String imgUrl, String description, Long spotId, Long fromId, PhotoFrom photoFrom, Long authorId) {
        this.imgUrl = imgUrl;
        this.description = description;
        this.spotId = spotId;
        this.fromId = fromId;
        this.photoFrom = photoFrom;
        this.authorId = authorId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public PhotoFrom getPhotoFrom() {
        return photoFrom;
    }

    public void setPhotoFrom(PhotoFrom photoFrom) {
        this.photoFrom = photoFrom;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public enum PhotoFrom {
        TRAVEL_NOTES
    }

}
