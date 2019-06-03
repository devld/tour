package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.dto.travel.TravelNotesDetailsOut;
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

    @Request("创建游记")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public TravelNotes createTravelNotes(@RequestBody @Valid TravelNotesIn travelNotesIn) {
        return travelNotesService.createTravelNotes(travelNotesIn, SecurityUtil.userId());
    }

    @Request("获取游记详情")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{notesId}")
    public TravelNotesDetailsOut getTravelNotesDetails(@PathVariable("notesId") long id) {
        return travelNotesService.getTravelNotesDetails(id, null);
    }

    @Request("编辑游记")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{notesId}")
    public TravelNotes editTravelNotes(@PathVariable("notesId") long id, @RequestBody @Valid TravelNotesIn travelNotesIn) {
        return travelNotesService.editTravelNotes(id, travelNotesIn, SecurityUtil.userId());
    }

    @Request("删除游记")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{notesId}")
    public void deleteTravelNotes(@PathVariable("notesId") long id) {
        travelNotesService.deleteTravelNotes(id, SecurityUtil.userId());
    }

    @Request("分享游记")
    @PostMapping("/{notesId}/share")
    public void shareTravelNotes(@PathVariable("notesId") long id) {
        travelNotesService.shareTravelNotes(id, SecurityUtil.userIdOrNull());
    }

    @Request("游记点赞")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{notesId}/like")
    public void toggleLike(@PathVariable("notesId") long notesId, @RequestParam("s") boolean state) {
        travelNotesService.likeTravelNotes(notesId, SecurityUtil.userId(), state);
    }

    @Request("收藏游记")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{notesId}/collect")
    public void toggleLikeSpotPhoto(@PathVariable("notesId") long notesId, @RequestParam("s") boolean state) {
        travelNotesService.collectTravelNotes(notesId, SecurityUtil.userId(), state);
    }
}
