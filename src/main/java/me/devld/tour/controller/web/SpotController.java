package me.devld.tour.controller.web;

import me.devld.tour.dto.PageParam;
import me.devld.tour.entity.Spot;
import me.devld.tour.service.SpotService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("webSpotController")
@RequestMapping("/spot")
public class SpotController {

    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
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
            spots = spotService.getSpotsByLocationId(locationId, pageParam);
        }

        model.addAttribute("spots", spots);

        return "spot/spotList";
    }

}
