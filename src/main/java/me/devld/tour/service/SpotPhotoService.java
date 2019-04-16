package me.devld.tour.service;

public interface SpotPhotoService {

    /**
     * 用户点赞/取消点赞照片
     *
     * @param photoId photoId
     * @param userId  userId
     * @param state   点赞/取消
     */
    void likePhoto(long photoId, long userId, boolean state);

}
