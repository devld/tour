package me.devld.tour.service;

import me.devld.tour.dto.DistrictDetail;
import me.devld.tour.entity.District;

import java.util.List;

public interface DistrictService {

    String getFullName(int id);

    List<District> getFullDistrict(int id);

    List<District> getChildren(int id);

    DistrictDetail getTree();

    List<District> getFlattingChildren(int id);

    District getById(int id);

}
