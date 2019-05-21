package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.travel.TravelNotesDetailsOut;
import me.devld.tour.dto.travel.TravelNotesIn;
import me.devld.tour.entity.TravelNotes;
import org.springframework.data.domain.Page;

public interface TravelNotesService {

    /**
     * 创建一篇游记
     *
     * @param travelNotes 游记内容
     * @param authorId    作者 userId
     * @return travelNotes
     */
    TravelNotes createTravelNotes(TravelNotesIn travelNotes, long authorId);

    /**
     * 获取游记详情
     *
     * @param id     游记 id
     * @param userId 当前用户 id
     * @return 游记详情
     */
    TravelNotesDetailsOut getTravelNotesDetails(long id, Long userId);

    /**
     * 根据景点获取游记
     *
     * @param spotId    景点 id
     * @param pageParam page
     * @return travelNotes
     */
    Page<TravelNotesDetailsOut> getTravelNotesBySpot(long spotId, PageParam pageParam);

    /**
     * 获取热门游记列表
     *
     * @param pageParam 分页参数
     * @return 游记列表
     */
    Page<TravelNotesDetailsOut> getHotTravelNotes(PageParam pageParam);

    /**
     * 用户分享 travelNotes
     *
     * @param travelNotesId travelNotesId
     * @param userId        userId
     */
    void shareTravelNotes(long travelNotesId, long userId);

    /**
     * 用户切换点赞 travelNotes
     *
     * @param travelNotesId travelNotesId
     * @param userId        userId
     * @param state         点赞/取消
     */
    void likeTravelNotes(long travelNotesId, long userId, boolean state);

    /**
     * 用户收藏 travelNotes
     *
     * @param travelNotesId travelNotesId
     * @param userId        userId
     * @param state         收藏/取消
     */
    void collectTravelNotes(long travelNotesId, long userId, boolean state);

    /**
     * 删除游记
     *
     * @param travelNotesId 游记 id
     * @param userId        操作用户
     */
    void deleteTravelNotes(long travelNotesId, long userId);

}
