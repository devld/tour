package me.devld.tour.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tour_travel_notes")
public class TravelNotes extends BaseEntity {

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    private Long authorId;

    private Integer likeCount;

    private Integer collectCount;

    private Integer shareCount;

    private Long startTime;

    private Integer daysUsed;

    private Integer costAverage;

    @ManyToMany
    @JoinTable(
            name = "tour_travel_notes_spot_rel",
            joinColumns = @JoinColumn(name = "travel_notes_id"),
            inverseJoinColumns = @JoinColumn(name = "spot_id")
    )
    private List<Spot> spots;

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

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getDaysUsed() {
        return daysUsed;
    }

    public void setDaysUsed(Integer daysUsed) {
        this.daysUsed = daysUsed;
    }

    public Integer getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(Integer costAverage) {
        this.costAverage = costAverage;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}
