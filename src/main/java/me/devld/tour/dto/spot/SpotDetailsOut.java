package me.devld.tour.dto.spot;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotPhoto;
import me.devld.tour.entity.SpotTicket;

import java.util.List;

public class SpotDetailsOut {

    @JsonUnwrapped
    private Spot spot;

    private List<SpotTicket> tickets;

    private List<SpotPhoto> photos;

    private Boolean went;

    private Boolean collected;

    public SpotDetailsOut() {
    }

    public SpotDetailsOut(Spot spot, List<SpotTicket> tickets, List<SpotPhoto> photos) {
        this.spot = spot;
        this.tickets = tickets;
        this.photos = photos;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public List<SpotTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<SpotTicket> tickets) {
        this.tickets = tickets;
    }

    public List<SpotPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<SpotPhoto> photos) {
        this.photos = photos;
    }

    public Boolean getWent() {
        return went;
    }

    public void setWent(Boolean went) {
        this.went = went;
    }

    public Boolean getCollected() {
        return collected;
    }

    public void setCollected(Boolean collected) {
        this.collected = collected;
    }
}
