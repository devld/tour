package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.travel.TravelNotesIn;
import me.devld.tour.entity.TravelNotes;
import me.devld.tour.service.TravelNotesService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ApiController
@RequestMapping("/travel")
public class TravelNotesController {

    private final TravelNotesService travelNotesService;

    public TravelNotesController(TravelNotesService travelNotesService) {
        this.travelNotesService = travelNotesService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public TravelNotes createTravelNotes(@RequestBody @Valid TravelNotesIn travelNotesIn) {
        return travelNotesService.createTravelNotes(travelNotesIn, SecurityUtil.userId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{notesId}/share")
    public void shareTravelNotes(@PathVariable("notesId") long id) {
        travelNotesService.shareTravelNotes(id, SecurityUtil.userId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{notesId}/like")
    public void toggleLike(@PathVariable("notesId") long notesId, @RequestParam("s") boolean state) {
        travelNotesService.likeTravelNotes(notesId, SecurityUtil.userId(), state);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{notesId}/collect")
    public void toggleLikeSpotPhoto(@PathVariable("notesId") long notesId, @RequestParam("s") boolean state) {
        travelNotesService.collectTravelNotes(notesId, SecurityUtil.userId(), state);
    }
}
