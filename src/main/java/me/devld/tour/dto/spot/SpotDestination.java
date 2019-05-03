package me.devld.tour.dto.spot;

import me.devld.tour.entity.District;

import java.util.List;

public class SpotDestination {

    private District district;

    private List<District> children;

    public SpotDestination() {
    }

    public SpotDestination(District district, List<District> children) {
        this.district = district;
        this.children = children;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public List<District> getChildren() {
        return children;
    }

    public void setChildren(List<District> children) {
        this.children = children;
    }
}
