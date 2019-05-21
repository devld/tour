package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotCommentIn;
import me.devld.tour.dto.spot.SpotCommentOut;
import me.devld.tour.entity.SpotComment;
import org.springframework.data.domain.Page;

public interface SpotCommentService {

    /**
     * 获取景点的评论
     *
     * @param id        景点 id
     * @param pageParam page
     * @param userId    当前用户 id
     * @return 评论列表
     */
    Page<SpotCommentOut> getSpotComments(long id, PageParam pageParam, Long userId);

    /**
     * 评论景点
     *
     * @param comment  评论内容
     * @param spotId   景点 id
     * @param authorId 作者 id
     * @return spotComment
     */
    SpotComment commentSpot(SpotCommentIn comment, long spotId, long authorId);

    /**
     * 用户点赞景点的评论
     *
     * @param commentId 评论 id
     * @param userId    用户 id
     * @param state     点赞/取消
     */
    void likeSpotComment(long commentId, long userId, boolean state);

    /**
     * 删除景点评论
     *
     * @param commentId 评论 id
     * @param userId    操作用户
     */
    void deleteComment(long commentId, long userId);

}
