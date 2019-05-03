package me.devld.tour.controller.web;


import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.travel.TravelNotesDetailsOut;
import me.devld.tour.service.SpotService;
import me.devld.tour.service.TravelNotesService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("webTravelNotesController")
@RequestMapping("/travel")
public class TravelNotesController {

    private final TravelNotesService travelNotesService;
    private final SpotService spotService;

    public TravelNotesController(TravelNotesService travelNotesService, SpotService spotService) {
        this.travelNotesService = travelNotesService;
        this.spotService = spotService;
    }

    @GetMapping("/spot/{spotId}")
    public String travelNotesList(@PathVariable("spotId") long spotId, PageParam pageParam, Model model) {

        Page<TravelNotesDetailsOut> travelNotes = travelNotesService.getTravelNotesBySpot(spotId, pageParam);
        model.addAttribute("notes", travelNotes);
        model.addAttribute("spot", spotService.getSpotById(spotId));

        return "travel-notes/notesList";
    }

    @GetMapping("/{id}")
    public String travelNotesDetail(@PathVariable("id") long notesId, Model model) {
        model.addAttribute("note", travelNotesService.getTravelNotesDetails(notesId, SecurityUtil.userIdOrNull()));
        return "travel-notes/notesDetail";
    }

}
