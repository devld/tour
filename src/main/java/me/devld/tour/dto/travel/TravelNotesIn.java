package me.devld.tour.dto.travel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class TravelNotesIn {

    @NotEmpty
    @Size(max = 128)
    private String title;

    private String content;

    private Long startTime;

    private Float daysUsed;

    private Integer costAverage;

    private List<Long> spotIds;

    private List<TravelNotesPhoto> photos;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Float getDaysUsed() {
        return daysUsed;
    }

    public void setDaysUsed(Float daysUsed) {
        this.daysUsed = daysUsed;
    }

    public Integer getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(Integer costAverage) {
        this.costAverage = costAverage;
    }

    public List<Long> getSpotIds() {
        return spotIds;
    }

    public void setSpotIds(List<Long> spotIds) {
        this.spotIds = spotIds;
    }

    public List<TravelNotesPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<TravelNotesPhoto> photos) {
        this.photos = photos;
    }

    public static class TravelNotesPhoto {
        private Long spotId;
        private String src;
        @Size(max = 64)
        private String alt;

        public Long getSpotId() {
            return spotId;
        }

        public void setSpotId(Long spotId) {
            this.spotId = spotId;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }
    }
}
