package me.devld.tour.dto.spot;

import me.devld.tour.entity.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SpotIn {

    @NotEmpty
    @Size(max = 64)
    private String name;

    private String intro;

    @Size(max = 128)
    private String coverUrl;

    private Location location;

    @Size(max = 64)
    private String phone;

    @Size(max = 64)
    private String website;

    @Size(max = 16)
    private String timeUsage;

    @Size(max = 128)
    private String transport;

    @Size(max = 128)
    private String openTime;

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
}
