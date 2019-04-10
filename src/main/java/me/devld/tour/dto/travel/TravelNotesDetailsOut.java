package me.devld.tour.dto.travel;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.TravelNotes;

import java.util.List;

public class TravelNotesDetailsOut {

    @JsonUnwrapped
    private TravelNotes travelNotes;

    private List<Spot> spots;

    public TravelNotesDetailsOut() {
    }

    public TravelNotesDetailsOut(TravelNotes travelNotes, List<Spot> spots) {
        this.travelNotes = travelNotes;
        this.spots = spots;
    }

    public TravelNotes getTravelNotes() {
        return travelNotes;
    }

    public void setTravelNotes(TravelNotes travelNotes) {
        this.travelNotes = travelNotes;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}
