package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.ApiResult;
import me.devld.tour.service.DistrictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiController
@RequestMapping("/district")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("/{id}")
    public ApiResult resolveDistrict(@PathVariable("id") int id) {
        return ApiResult.ok(districtService.getFullDistrict(id));
    }

    @GetMapping("/{id}/children")
    public ApiResult getChildren(@PathVariable("id") int id) {
        return ApiResult.ok(districtService.getChildren(id));
    }

}
