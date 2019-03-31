package me.devld.tour.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.entity.District;

import java.util.LinkedList;
import java.util.List;

public class DistrictDetail {

    @JsonIgnore
    private DistrictDetail parent;

    @JsonUnwrapped
    private District district;

    private List<DistrictDetail> children;

    public DistrictDetail(District district) {
        this.district = district;
        this.children = new LinkedList<>();
    }

    public DistrictDetail getParent() {
        return parent;
    }

    public void setParent(DistrictDetail parent) {
        this.parent = parent;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public List<DistrictDetail> getChildren() {
        return children;
    }

    public void setChildren(List<DistrictDetail> children) {
        this.children = children;
    }

    public void addChild(DistrictDetail district) {
        this.children.add(district);
    }
}
