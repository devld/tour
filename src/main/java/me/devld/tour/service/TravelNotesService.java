package me.devld.tour.service;

import me.devld.tour.dto.travel.TravelNotesDetailsOut;
import me.devld.tour.dto.travel.TravelNotesIn;
import me.devld.tour.entity.TravelNotes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param id 游记 id
     * @return 游记详情
     */
    TravelNotesDetailsOut getTravelNotesDetails(long id);

    /**
     * 根据景点获取游记
     *
     * @param spotId 景点 id
     * @return travelNotes
     */
    Page<TravelNotes> getTravelNotesBySpot(long spotId, Pageable pageable);

    /**
     * 用户分享 travelNotes
     *
     * @param travelNotesId travelNotesId
     * @param userId        userId
     */
    void shareTravelNotes(long travelNotesId, long userId);

    /**
     * 用户点赞 travelNotes
     *
     * @param travelNotesId travelNotesId
     * @param userId        userId
     */
    void likeTravelNotes(long travelNotesId, long userId);

    /**
     * 用户收藏 travelNotes
     *
     * @param travelNotesId travelNotesId
     * @param userId        userId
     */
    void collectTravelNotes(long travelNotesId, long userId);

}
