package me.devld.tour.repository;

import me.devld.tour.entity.SpotPhoto;
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

}
