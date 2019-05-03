package me.devld.tour.util;

public class ImageProp {
    private String src;
    private String alt;

    ImageProp(String src, String alt) {
        this.src = src;
        this.alt = alt;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    @Override
    public String toString() {
        return "ImageProp{" +
                "src='" + src + '\'' +
                ", alt='" + alt + '\'' +
                '}';
    }
}
