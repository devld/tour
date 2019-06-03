package me.devld.tour.repository;

import me.devld.tour.entity.BaseStateEntity;
import me.devld.tour.entity.SpotPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotPhotoRepository extends BaseStateRepository<SpotPhoto> {

    @Modifying
    @Query("UPDATE SpotPhoto sp SET sp.likeCount = sp.likeCount + ?2 WHERE sp.id = ?1 AND sp.state <> " + BaseStateEntity.STATE_DELETED)
    int incrementCountById(long id, int likeCount);

    @Query(value = "SELECT sp.* FROM tour_spot_photo sp WHERE sp.spot_id = ?1 AND sp.state <> " +
            BaseStateEntity.STATE_DELETED + " ORDER BY sp.like_count DESC LIMIT 3", nativeQuery = true)
    List<SpotPhoto> findTop3BySpotIdOrderByLikeCountDesc(long spotId);

    @Query("SELECT sp FROM SpotPhoto sp WHERE sp.spotId = ?1 AND sp.state <> " + BaseStateEntity.STATE_DELETED)
    Page<SpotPhoto> findAllBySpotId(long spotId, Pageable pageable);

    @Query(value = "SELECT id FROM tour_spot_photo WHERE id < ?1 AND spot_id = ?2 AND state <> " +
            BaseStateEntity.STATE_DELETED + " ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Long findPreId(long id, long spotId);

    @Query(value = "SELECT id FROM tour_spot_photo WHERE id > ?1 AND spot_id = ?2 AND state <> " +
            BaseStateEntity.STATE_DELETED + " ORDER BY id ASC LIMIT 1", nativeQuery = true)
    Long findNextId(long id, long spotId);

    @Modifying
    @Query("UPDATE SpotPhoto sp SET sp.state = " + BaseStateEntity.STATE_DELETED + " WHERE sp.fromId = ?1 AND sp.photoFrom = ?2")
    int deleteAllByFromIdAndPhotoFrom(long fromId, SpotPhoto.PhotoFrom from);

}
