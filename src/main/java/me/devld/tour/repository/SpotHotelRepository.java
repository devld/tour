package me.devld.tour.repository;

import me.devld.tour.entity.SpotHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotHotelRepository extends JpaRepository<SpotHotel, Long> {
}
