package me.devld.tour.repository;

import me.devld.tour.entity.SpotPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotPhotoRepository extends JpaRepository<SpotPhoto, Long> {

    @Modifying
    @Query("UPDATE SpotPhoto sp SET sp.likeCount = sp.likeCount + ?2 WHERE sp.id = ?1")
    int incrementCountById(long id, int likeCount);

    List<SpotPhoto> findTop3BySpotIdOrderByLikeCountDesc(long spotId);

    Page<SpotPhoto> findAllBySpotId(long spotId, Pageable pageable);

    @Query(value = "SELECT id FROM tour_spot_photo WHERE id < ?1 AND spot_id = ?2 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Long findPreId(long id, long spotId);

    @Query(value = "SELECT id FROM tour_spot_photo WHERE id > ?1 AND spot_id = ?2 ORDER BY id ASC LIMIT 1", nativeQuery = true)
    Long findNextId(long id, long spotId);

}
