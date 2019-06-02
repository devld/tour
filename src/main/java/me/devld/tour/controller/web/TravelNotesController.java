package me.devld.tour.controller.web;


import me.devld.tour.controller.Request;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.travel.TravelNotesDetailsOutVO;
import me.devld.tour.service.SpotService;
import me.devld.tour.service.TravelNotesService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("webTravelNotesController")
@RequestMapping("/travel")
public class TravelNotesController {

    private final TravelNotesService travelNotesService;
    private final SpotService spotService;

    public TravelNotesController(TravelNotesService travelNotesService, SpotService spotService) {
        this.travelNotesService = travelNotesService;
        this.spotService = spotService;
    }

    @Request("景点游记列表页面")
    @GetMapping("/spot/{spotId}")
    public String travelNotesList(@PathVariable("spotId") long spotId, PageParam pageParam, Model model) {

        model.addAttribute("notes", travelNotesService.getTravelNotesBySpot(spotId, pageParam).map(TravelNotesDetailsOutVO::from));
        model.addAttribute("spot", spotService.getSpotWithTicketsById(spotId));
        model.addAttribute("title", "spot");

        return "travel-notes/notesList";
    }

    @Request("热门游记页面")
    @GetMapping("/hot")
    public String hotTravelNotesList(PageParam pageParam, Model model) {

        model.addAttribute("title", "hot");
        model.addAttribute("notes", travelNotesService.getHotTravelNotes(pageParam).map(TravelNotesDetailsOutVO::from));

        return "travel-notes/notesList";
    }

    @Request("游记详情页面")
    @GetMapping("/{id}")
    public String travelNotesDetail(@PathVariable("id") long notesId, Model model) {
        model.addAttribute("note", TravelNotesDetailsOutVO.from(travelNotesService.getTravelNotesDetails(notesId, SecurityUtil.userIdOrNull())));
        return "travel-notes/notesDetail";
    }

    @Request("编辑游记页面")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String editTravelNotes(@RequestParam(value = "id", required = false) Long notesId, Model model) {
        model.addAttribute("id", notesId);
        return "travel-notes/notesEdit";
    }

}
