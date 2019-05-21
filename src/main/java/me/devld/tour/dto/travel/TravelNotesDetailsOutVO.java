package me.devld.tour.dto.travel;

import me.devld.tour.util.HtmlUtils;
import me.devld.tour.util.ImageProp;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class TravelNotesDetailsOutVO extends TravelNotesDetailsOut {

    private String shortContent;

    private String coverUrl;

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public static TravelNotesDetailsOutVO from(TravelNotesDetailsOut detail) {
        TravelNotesDetailsOutVO vo = new TravelNotesDetailsOutVO();
        BeanUtils.copyProperties(detail, vo);
        String content = detail.getTravelNotes().getContent();
        List<ImageProp> images = HtmlUtils.extractImagesFromHtml(content);
        if (!images.isEmpty()) {
            vo.setCoverUrl(images.get(0).getSrc());
        }
        String shortContent = HtmlUtils.htmlToText(content);
        if (shortContent.length() > 100) {
            shortContent = shortContent.substring(0, 100) + "...";
        }
        vo.setShortContent(shortContent);
        return vo;
    }
}
