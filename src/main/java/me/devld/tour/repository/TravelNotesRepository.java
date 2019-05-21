package me.devld.tour.repository;

import me.devld.tour.entity.Spot;
import me.devld.tour.entity.TravelNotes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelNotesRepository extends JpaRepository<TravelNotes, Long> {

    @Query("SELECT tn FROM TravelNotes tn LEFT JOIN tn.spots tns WHERE tns.id = ?1 AND tn.state = 0")
    Page<TravelNotes> findAllBySpots(long spotId, Pageable pageable);

    @Modifying
    @Query("UPDATE TravelNotes tn SET tn.likeCount = tn.likeCount + ?2, tn.collectCount = tn.collectCount + ?3, tn.shareCount = tn.shareCount + ?4 WHERE tn.id = ?1 AND tn.state = 0")
    int incrementCountById(long id, int like, int collect, int share);

    @Query("SELECT tn FROM TravelNotes tn WHERE tn.state = 0")
    @Override
    Page<TravelNotes> findAll(Pageable pageable);

    @Query("UPDATE TravelNotes tn SET tn.state = 1 WHERE tn.id = ?1")
    @Modifying
    int softDeleteById(long travelNotesId);

}
