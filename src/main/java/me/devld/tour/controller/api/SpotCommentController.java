package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotCommentIn;
import me.devld.tour.dto.spot.SpotCommentOut;
import me.devld.tour.entity.SpotComment;
import me.devld.tour.service.SpotCommentService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ApiController
@RequestMapping("/spot")
public class SpotCommentController {

    private final SpotCommentService spotCommentService;

    public SpotCommentController(SpotCommentService spotCommentService) {
        this.spotCommentService = spotCommentService;
    }

    @Request("评论景点")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{spotId}/comment")
    public SpotComment commentSpot(@PathVariable("spotId") long spotId, @RequestBody @Valid SpotCommentIn spotCommentIn) {
        return spotCommentService.commentSpot(spotCommentIn, spotId, SecurityUtil.userId());
    }

    @Request("评论点赞")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/{commentId}/like")
    public void toggleCommentLike(@PathVariable("commentId") long commendId, @RequestParam("s") boolean state) {
        spotCommentService.likeSpotComment(commendId, SecurityUtil.userId(), state);
    }

    @Request("获取景点评论")
    @GetMapping("/{spotId}/comment")
    public Page<SpotCommentOut> getSpotComments(@PathVariable("spotId") long spotId, PageParam pageParam) {
        return spotCommentService.getSpotComments(spotId, pageParam, SecurityUtil.userIdOrNull());
    }

    @Request("删除评论")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable("commentId") long commendId) {
        spotCommentService.deleteComment(commendId, SecurityUtil.userId());
    }

}
