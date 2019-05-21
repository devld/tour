package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.Spot;
import me.devld.tour.service.SpotService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.data.domain.Page;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public Spot createSpot(@RequestBody SpotIn spotIn) {
        return spotService.createSpot(spotIn);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{spotId}")
    public Spot updateSpot(@PathVariable("spotId") long spotId, @RequestBody SpotIn spotIn) {
        return spotService.updateSpot(spotId, spotIn);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{spotId}")
    public void deleteSpot(@PathVariable("spotId") long spotId) {
        spotService.deleteSpot(spotId, SecurityUtil.userId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{spotId}")
    public SpotDetailsOut getSpot(@PathVariable("spotId") long spotId) {
        return spotService.getSpotById(spotId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public Page<Spot> searchSpot(@RequestParam("q") String query, PageParam pageParam) {
        return spotService.searchSpot(query, pageParam);
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
}
