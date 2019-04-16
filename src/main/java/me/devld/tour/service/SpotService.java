package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotCommentIn;
import me.devld.tour.dto.spot.SpotCommentOut;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotComment;
import org.springframework.data.domain.Page;

public interface SpotService {

    /**
     * 创建景点
     *
     * @param spot spot
     * @return spot
     */
    Spot createSpot(SpotIn spot);

    /**
     * 更新景点
     *
     * @param spotId 景点 id
     * @param spot   spot
     * @return spot
     */
    Spot updateSpot(long spotId, SpotIn spot);

    /**
     * 获取景点的详情
     *
     * @param id 景点的 id
     * @return 景点详细信息
     */
    SpotDetailsOut getSpotDetails(long id);

    /**
     * 获取景点的评论
     *
     * @param id        景点 id
     * @param pageParam page
     * @return 评论列表
     */
    Page<SpotCommentOut> getSpotComments(long id, PageParam pageParam);

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
     * 根据 {@link me.devld.tour.entity.District} 的 id 获取该地(及其子地域)的所有景点
     *
     * @param id        districtId
     * @param pageParam page
     * @return spots
     */
    Page<Spot> getSpotsByLocationId(int id, PageParam pageParam);

    /**
     * 获取景点列表
     *
     * @param pageParam page
     * @return 景点列表
     */
    Page<Spot> getSpotList(PageParam pageParam);

    /**
     * 用户收藏景点
     *
     * @param id     景点 id
     * @param userId 用户 id
     * @param state  收藏/取消收藏
     */
    void collectSpot(long id, long userId, boolean state);

    /**
     * 用户标记去过景点
     *
     * @param id     景点 id
     * @param userId 用户 id
     * @param state  标记/取消
     */
    void wentSpot(long id, long userId, boolean state);

    /**
     * 用户点赞景点的评论
     *
     * @param commentId 评论 id
     * @param userId    用户 id
     * @param state     点赞/取消
     */
    void likeSpotComment(long commentId, long userId, boolean state);

}
