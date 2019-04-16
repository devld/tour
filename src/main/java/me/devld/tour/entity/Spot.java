package me.devld.tour.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tour_spot")
public class Spot extends BaseEntity {

    private String name;

    @Column(columnDefinition = "text")
    private String intro;

    private String coverUrl;

    @Embedded
    private Location location;

    private String phone;

    private String website;

    private String timeUsage;

    private String transport;

    private String openTime;

    private Integer wentCount = 0;

    private Integer collectCount = 0;

    private Integer photoCount = 0;

    private Integer commentCount = 0;

    public Spot() {
    }

    public Spot(long id) {
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTimeUsage() {
        return timeUsage;
    }

    public void setTimeUsage(String timeUsage) {
        this.timeUsage = timeUsage;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public Integer getWentCount() {
        return wentCount;
    }

    public void setWentCount(Integer wentCount) {
        this.wentCount = wentCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(Integer photoCount) {
        this.photoCount = photoCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", location=" + location +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", timeUsage='" + timeUsage + '\'' +
                ", transport='" + transport + '\'' +
                ", openTime='" + openTime + '\'' +
                ", wentCount=" + wentCount +
                ", collectCount=" + collectCount +
                ", photoCount=" + photoCount +
                ", commentCount=" + commentCount +
                '}';
    }
}
