package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
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

    @GetMapping("/{id}")
    public List<District> resolveDistrict(@PathVariable("id") int id) {
        return districtService.getFullDistrict(id);
    }

    @GetMapping("/{id}/children")
    public List<District> getChildren(@PathVariable("id") int id) {
        return districtService.getChildren(id);
    }

}
