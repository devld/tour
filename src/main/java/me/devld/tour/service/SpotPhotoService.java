package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import me.devld.tour.entity.SpotPhoto;
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
     * @param pageParam 分页
     * @return 照片列表
     */
    Page<SpotPhoto> getSpotPhotosBySpot(long spotId, PageParam pageParam);

}
