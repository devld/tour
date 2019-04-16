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

    Page<TravelNotes> findAllBySpots(Spot spot, Pageable pageable);

    @Modifying
    @Query("UPDATE TravelNotes tn SET tn.likeCount = tn.likeCount + ?2, tn.collectCount = tn.collectCount + ?3, tn.shareCount = tn.shareCount + ?4 WHERE tn.id = ?1")
    int incrementCountById(long id, int like, int collect, int share);

}
