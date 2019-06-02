package me.devld.tour.dto.spot;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.entity.SpotPhoto;

public class SpotPhotoOut {

    @JsonUnwrapped
    private SpotPhoto photo;

    private Boolean liked;

    public SpotPhotoOut(SpotPhoto photo, Boolean liked) {
        this.photo = photo;
        this.liked = liked;
    }

    public SpotPhotoOut() {
    }

    public SpotPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(SpotPhoto photo) {
        this.photo = photo;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

}
