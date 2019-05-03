package me.devld.tour.dto.travel;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.TravelNotes;

import java.util.List;

public class TravelNotesDetailsOut {

    @JsonUnwrapped
    private TravelNotes travelNotes;

    private UserProfile author;

    private List<Spot> spots;

    private String coverUrl;

    private String shortContent;

    private Boolean liked;

    private Boolean collected;

    public TravelNotesDetailsOut() {
    }

    public TravelNotesDetailsOut(TravelNotes travelNotes, UserProfile author, List<Spot> spots) {
        this.travelNotes = travelNotes;
        this.author = author;
        this.spots = spots;
    }

    public TravelNotes getTravelNotes() {
        return travelNotes;
    }

    public void setTravelNotes(TravelNotes travelNotes) {
        this.travelNotes = travelNotes;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public void setAuthor(UserProfile author) {
        this.author = author;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getCollected() {
        return collected;
    }

    public void setCollected(Boolean collected) {
        this.collected = collected;
    }
}
