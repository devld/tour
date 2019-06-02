package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotPhotoOut;
import me.devld.tour.service.SpotPhotoService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping("/spot/photo")
public class SpotPhotoController {

    private final SpotPhotoService spotPhotoService;

    public SpotPhotoController(SpotPhotoService spotPhotoService) {
        this.spotPhotoService = spotPhotoService;
    }

    @Request("景点照片点赞")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{photoId}/like")
    public void toggleLikeSpotPhoto(@PathVariable("photoId") long photoId, @RequestParam("s") boolean state) {
        spotPhotoService.likePhoto(photoId, SecurityUtil.userId(), state);
    }

    @Request("获取景点照片")
    @GetMapping
    public Page<SpotPhotoOut> getSpotPhotos(@RequestParam("spotId") long spotId, PageParam pageParam) {
        return spotPhotoService.getSpotPhotosBySpot(spotId, SecurityUtil.userIdOrNull(), pageParam);
    }

}
