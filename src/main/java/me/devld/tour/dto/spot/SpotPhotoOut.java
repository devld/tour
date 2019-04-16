package me.devld.tour.dto.spot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotPhoto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpotPhotoOut {

    @JsonUnwrapped
    private SpotPhoto photo;

    private UserProfile author;

    private Spot spot;

    private Object from;

    public SpotPhotoOut() {
    }

    public SpotPhotoOut(SpotPhoto photo, UserProfile author, Spot spot, Object from) {
        this.photo = photo;
        this.author = author;
        this.spot = spot;
        this.from = from;
    }

    public SpotPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(SpotPhoto photo) {
        this.photo = photo;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public void setAuthor(UserProfile author) {
        this.author = author;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }
}
