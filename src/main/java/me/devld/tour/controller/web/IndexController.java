package me.devld.tour.controller.web;

import me.devld.tour.service.SpotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final SpotService spotService;

    public IndexController(SpotService spotService) {
        this.spotService = spotService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("dest", spotService.getSpotDestinations());
        return "index";
    }

}
