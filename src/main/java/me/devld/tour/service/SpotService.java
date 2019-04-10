package me.devld.tour.service;

import me.devld.tour.dto.spot.SpotCommentIn;
import me.devld.tour.dto.spot.SpotCommentOut;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param spot spot
     * @return spot
     */
    Spot updateSpot(SpotIn spot);

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
     * @param id 景点 id
     * @return 评论列表
     */
    Page<SpotCommentOut> getSpotComments(long id, Pageable pageable);

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
     * @param id districtId
     * @return spots
     */
    Page<Spot> getSpotsByLocationId(int id, Pageable pageable);

    /**
     * 用户收藏景点
     *
     * @param id     景点 id
     * @param userId 用户 id
     */
    void collectSpot(long id, long userId);

    /**
     * 用户标记去过景点
     *
     * @param id     景点 id
     * @param userId 用户 id
     */
    void wentSpot(long id, long userId);

}
