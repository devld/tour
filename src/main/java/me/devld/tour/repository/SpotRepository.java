package me.devld.tour.repository;

import me.devld.tour.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    @Modifying
    @Query("UPDATE Spot SET wentCount = wentCount + ?2, collectCount = collectCount + ?3, commentCount = commentCount + ?4 WHERE id = ?1 AND state = 0")
    int incrementCountById(long id, int went, int collect, int comment);

    @Modifying
    @Query("UPDATE Spot SET photoCount = photoCount + ?2 WHERE id = ?1")
    int incrementPhotoCount(long id, int amount);

    @Query("SELECT s FROM Spot s WHERE s.location.locationId IN ?1 AND s.state = 0")
    Page<Spot> findAllByLocationLocationIdIn(List<Integer> locationIds, Pageable pageable);

    int countByIdIn(List<Long> ids);

    @Query("SELECT s FROM Spot s WHERE s.state = 0 AND s.name LIKE %?1%")
    Page<Spot> findAllByNameLike(String nameLike, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT DISTINCT location_id FROM tour_spot")
    List<Short> getDistinctLocationIds();

    @Query("SELECT s FROM Spot s WHERE s.state = 0")
    @Override
    Page<Spot> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Spot s SET s.state = 1 WHERE s.id = ?1 AND s.state = 0")
    int softDeleteById(long spotId);
}
