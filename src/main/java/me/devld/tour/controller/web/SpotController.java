package me.devld.tour.controller.web;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotPhoto;
import me.devld.tour.service.DistrictService;
import me.devld.tour.service.SpotService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller("webSpotController")
@RequestMapping("/spot")
public class SpotController {

    private final SpotService spotService;
    private final DistrictService districtService;

    public SpotController(SpotService spotService, DistrictService districtService) {
        this.spotService = spotService;
        this.districtService = districtService;
    }

    @GetMapping("")
    public String spotList(
            @RequestParam(value = "lid", required = false) Integer locationId,
            PageParam pageParam,
            Model model
    ) {

        Page<Spot> spots;
        if (locationId == null) {
            spots = spotService.getSpotList(pageParam);
        } else {
            model.addAttribute("district", districtService.getById(locationId));
            spots = spotService.getSpotsByLocationId(locationId, pageParam);
        }

        model.addAttribute("spots", spots);

        return "spot/spotList";
    }

    @GetMapping("/{spotId}")
    public String spotDetail(@PathVariable("spotId") long spotId, Model model) {
        SpotDetailsOut details = spotService.getSpotDetails(spotId, SecurityUtil.userIdOrNull());
        model.addAttribute("spot", details);
        List<SpotPhoto> photos = details.getPhotos();

        if (photos.size() > 0) {
            model.addAttribute("photo1", photos.get(0));
        }
        if (photos.size() > 1) {
            model.addAttribute("photo2", photos.get(1));
        }
        if (photos.size() > 2) {
            model.addAttribute("photo3", photos.get(2));
        }

        return "spot/spotDetail";
    }

}
