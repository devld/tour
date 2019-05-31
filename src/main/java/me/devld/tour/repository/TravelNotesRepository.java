package me.devld.tour.repository;

import me.devld.tour.entity.BaseStateEntity;
import me.devld.tour.entity.TravelNotes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelNotesRepository extends BaseStateRepository<TravelNotes> {

    @Query("SELECT tn FROM TravelNotes tn LEFT JOIN tn.spots tns WHERE " +
            "tns.id = ?1 AND tn.state <> " + BaseStateEntity.STATE_DELETED)
    Page<TravelNotes> findAllBySpots(long spotId, Pageable pageable);

    @Modifying
    @Query("UPDATE TravelNotes tn SET tn.likeCount = tn.likeCount + ?2, tn.collectCount = tn.collectCount + ?3, tn.shareCount = tn.shareCount + ?4 " +
            "WHERE tn.id = ?1 AND tn.state <> " + BaseStateEntity.STATE_DELETED)
    int incrementCountById(long id, int like, int collect, int share);

}
