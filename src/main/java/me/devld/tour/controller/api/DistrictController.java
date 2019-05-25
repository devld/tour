package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.entity.District;
import me.devld.tour.service.DistrictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ApiController
@RequestMapping("/district")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @Request("解析地区信息")
    @GetMapping("/{id}")
    public List<District> resolveDistrict(@PathVariable("id") int id) {
        return districtService.getFullDistrict(id);
    }

    @Request("获取地区的子地区")
    @GetMapping("/{id}/children")
    public List<District> getChildren(@PathVariable("id") int id) {
        return districtService.getChildren(id);
    }

    @Request("解析地区编码")
    @GetMapping("/code/{code}")
    public District getByCode(@PathVariable("code") String code) {
        return districtService.getByCode(code);
    }

}
