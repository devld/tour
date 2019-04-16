package me.devld.tour.repository;

import me.devld.tour.entity.SpotComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotCommentRepository extends JpaRepository<SpotComment, Long> {

    @Modifying
    @Query("UPDATE SpotComment sc SET sc.likeCount = sc.likeCount + ?2 WHERE sc.id = ?1")
    int incrementCountById(long id, int like);

    Page<SpotComment> findAllBySpotId(long spotId, Pageable pageable);

}
