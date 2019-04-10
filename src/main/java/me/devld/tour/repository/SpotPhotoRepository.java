package me.devld.tour.repository;

import me.devld.tour.entity.SpotPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotPhotoRepository extends JpaRepository<SpotPhoto, Long> {
}
