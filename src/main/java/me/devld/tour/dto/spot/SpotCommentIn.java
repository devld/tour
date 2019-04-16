package me.devld.tour.dto.spot;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SpotCommentIn {

    @NotEmpty
    @Size(max = 1000)
    private String content;

    public SpotCommentIn() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
