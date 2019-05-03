package me.devld.tour.dto.spot;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.entity.SpotComment;

public class SpotCommentOut {

    @JsonUnwrapped
    private SpotComment comment;

    private UserProfile author;

    private Boolean liked;

    public SpotCommentOut() {
    }

    public SpotCommentOut(SpotComment comment, UserProfile author) {
        this.comment = comment;
        this.author = author;
    }

    public SpotComment getComment() {
        return comment;
    }

    public void setComment(SpotComment comment) {
        this.comment = comment;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public void setAuthor(UserProfile author) {
        this.author = author;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}
