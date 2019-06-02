package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotPhotoDetailsOut;
import me.devld.tour.dto.spot.SpotPhotoOut;
import org.springframework.data.domain.Page;

public interface SpotPhotoService {

    /**
     * 用户点赞/取消点赞照片
     *
     * @param photoId photoId
     * @param userId  userId
     * @param state   点赞/取消
     */
    void likePhoto(long photoId, long userId, boolean state);

    /**
     * 获取景点的照片
     *
     * @param spotId    景点 id
     * @param userId    用户 id
     * @param pageParam 分页
     * @return 照片列表
     */
    Page<SpotPhotoOut> getSpotPhotosBySpot(long spotId, Long userId, PageParam pageParam);

    /**
     * 获取照片详情
     *
     * @param photoId 照片 id
     * @param userId  用户 id
     * @return 照片详情
     */
    SpotPhotoDetailsOut getPhotoDetails(long photoId, Long userId);

}
