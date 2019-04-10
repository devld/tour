package me.devld.tour.dto.travel;

import java.util.List;

public class TravelNotesIn {

    private String title;

    private String content;

    private Long startTime;

    private Integer daysUsage;

    private Integer costAverage;

    private List<Long> spotIds;

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

    public Integer getDaysUsage() {
        return daysUsage;
    }

    public void setDaysUsage(Integer daysUsage) {
        this.daysUsage = daysUsage;
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
}
