package me.devld.tour.repository;

import me.devld.tour.entity.SpotFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotFoodRepository extends JpaRepository<SpotFood, Long> {

}
