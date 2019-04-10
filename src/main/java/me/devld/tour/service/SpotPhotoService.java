package me.devld.tour.service;

public interface SpotPhotoService {

    /**
     * 用户点赞照片
     *
     * @param photoId photoId
     * @param userId  userId
     */
    void likePhoto(long photoId, long userId);


}
