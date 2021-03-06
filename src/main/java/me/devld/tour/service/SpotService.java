package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotDestination;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.Spot;
import org.springframework.data.domain.Page;

import java.util.List;

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
     * @param id     景点的 id
     * @param userId 当前用户 id
     * @return 景点详细信息
     */
    SpotDetailsOut getSpotDetails(long id, Long userId);

    /**
     * 搜索景点
     *
     * @param keywords 搜索词
     * @return 景点
     */
    Page<Spot> searchSpot(String keywords, PageParam pageParam);

    /**
     * 获取景点和景点门票
     *
     * @param id spotId
     * @return spot
     */
    SpotDetailsOut getSpotWithTicketsById(long id);

    /**
     * 获取景点
     *
     * @param id 景点 id
     * @return 景点
     */
    Spot getSpotById(long id);

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
     * 获取含有景点的地区列表
     *
     * @return 目的地列表
     */
    List<SpotDestination> getSpotDestinations();

    /**
     * 获取推荐景点
     *
     * @param userId    当前用户 id
     * @param pageParam 分页
     * @return 景点列表
     */
    Page<Spot> getRecommendSpots(long userId, PageParam pageParam);

    /**
     * 获取热门景点
     *
     * @param pageParam 分页
     * @return 景点列表
     */
    Page<Spot> getHotSpots(PageParam pageParam);

    /**
     * 删除景点
     *
     * @param spotId 景点 id
     * @param userId 操作用户
     */
    void deleteSpot(long spotId, long userId);

}
