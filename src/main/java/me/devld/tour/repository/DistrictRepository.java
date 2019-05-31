package me.devld.tour.repository;

import me.devld.tour.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {

    District findFirstByCode(String code);

}
