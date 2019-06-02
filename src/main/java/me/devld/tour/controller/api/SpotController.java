package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.Spot;
import me.devld.tour.service.SpotService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping("/spot")
public class SpotController {

    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    @Request("创建景点")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public Spot createSpot(@RequestBody SpotIn spotIn) {
        return spotService.createSpot(spotIn);
    }

    @Request("更新景点")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{spotId}")
    public Spot updateSpot(@PathVariable("spotId") long spotId, @RequestBody SpotIn spotIn) {
        return spotService.updateSpot(spotId, spotIn);
    }

    @Request("删除景点")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{spotId}")
    public void deleteSpot(@PathVariable("spotId") long spotId) {
        spotService.deleteSpot(spotId, SecurityUtil.userId());
    }

    @Request("获取景点(ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{spotId}")
    public SpotDetailsOut getSpot(@PathVariable("spotId") long spotId) {
        return spotService.getSpotWithTicketsById(spotId);
    }


    @Request("搜索景点")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public Page<Spot> searchSpot(@RequestParam("q") String query, PageParam pageParam) {
        return spotService.searchSpot(query, pageParam);
    }

    @Request("标记景点为去过")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{spotId}/went")
    public void toggleWent(@PathVariable("spotId") long spotId, @RequestParam("s") boolean state) {
        spotService.wentSpot(spotId, SecurityUtil.userId(), state);
    }

    @Request("收藏景点")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{spotId}/collect")
    public void toggleCollect(@PathVariable("spotId") long spotId, @RequestParam("s") boolean state) {
        spotService.collectSpot(spotId, SecurityUtil.userId(), state);
    }
}
