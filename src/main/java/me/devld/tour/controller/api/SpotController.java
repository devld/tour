package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.spot.SpotCommentIn;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotComment;
import me.devld.tour.service.SpotService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping("/spot")
public class SpotController {

    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("")
    public Spot createSpot(@RequestBody SpotIn spotIn) {
        return spotService.createSpot(spotIn);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{spotId}")
    public Spot updateSpot(@PathVariable("spotId") long spotId, @RequestBody SpotIn spotIn) {
        return spotService.updateSpot(spotId, spotIn);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{spotId}/comment")
    public SpotComment commentSpot(@PathVariable("spotId") long spotId, @RequestBody SpotCommentIn spotCommentIn) {
        return spotService.commentSpot(spotCommentIn, spotId, SecurityUtil.userId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{spotId}/went")
    public void toggleWent(@PathVariable("spotId") long spotId, @RequestParam("s") boolean state) {
        spotService.wentSpot(spotId, SecurityUtil.userId(), state);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{spotId}/collect")
    public void toggleCollect(@PathVariable("spotId") long spotId, @RequestParam("s") boolean state) {
        spotService.collectSpot(spotId, SecurityUtil.userId(), state);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/spot/comment/{commentId}/like")
    public void toggleCommentLike(@PathVariable("commentId") long commendId, @RequestParam("s") boolean state) {
        spotService.likeSpotComment(commendId, SecurityUtil.userId(), state);
    }

}