package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.service.SpotPhotoService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ApiController
@RequestMapping("/spot/photo")
public class SpotPhotoController {

    private final SpotPhotoService spotPhotoService;

    public SpotPhotoController(SpotPhotoService spotPhotoService) {
        this.spotPhotoService = spotPhotoService;
    }

    @Request("景点照片点赞")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/photo/{photoId}/like")
    public void toggleLikeSpotPhoto(@PathVariable("photoId") long photoId, @RequestParam("s") boolean state) {
        spotPhotoService.likePhoto(photoId, SecurityUtil.userId(), state);
    }

}
