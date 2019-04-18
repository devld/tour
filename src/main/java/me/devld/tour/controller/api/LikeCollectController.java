package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@ApiController
@RequestMapping("/userMark")
public class LikeCollectController {

    private final LikeCollectService likeCollectService;

    public LikeCollectController(LikeCollectService likeCollectService) {
        this.likeCollectService = likeCollectService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public List<LikeCollectRel> getRel(
            @RequestParam("type") RelObjectType objType,
            @RequestParam("rel") RelType relType,
            @RequestParam("ids") List<Long> ids
    ) {
        return likeCollectService.getRelBy(SecurityUtil.userId(), objType, relType, ids);
    }

}
