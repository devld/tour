package me.devld.tour.controller.web;

import me.devld.tour.controller.Request;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.travel.TravelNotesDetailsOutVO;
import me.devld.tour.service.SpotService;
import me.devld.tour.service.TravelNotesService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final SpotService spotService;
    private final TravelNotesService travelNotesService;

    public IndexController(SpotService spotService, TravelNotesService travelNotesService) {
        this.spotService = spotService;
        this.travelNotesService = travelNotesService;
    }

    @Request("首页")
    @GetMapping("/")
    public String index(Model model) {
        // 目的地列表
        model.addAttribute("dest", spotService.getSpotDestinations());

        // 热门景点
        model.addAttribute("hotSpots",
                spotService.getHotSpots(PageParam.from(1, 5))
        );

        // 热门游记
        model.addAttribute("hotNotes",
                travelNotesService.getHotTravelNotes(PageParam.from(1, 5)).map(TravelNotesDetailsOutVO::from)
        );

        Long userId = SecurityUtil.userIdOrNull();
        if (userId != null) {
            // 推荐景点
            model.addAttribute("recommendSpots",
                    spotService.getRecommendSpots(userId, PageParam.from(1, 5))
            );
        }

        return "index";
    }

}
