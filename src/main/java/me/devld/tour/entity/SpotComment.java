package me.devld.tour.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tour_spot_comment")
public class SpotComment extends BaseEntity {

    private Long spotId;

    private Long authorId;

    private String content;

    private Integer likeCount;

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
