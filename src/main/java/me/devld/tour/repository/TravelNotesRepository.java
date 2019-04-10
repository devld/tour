package me.devld.tour.repository;

import me.devld.tour.entity.TravelNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelNotesRepository extends JpaRepository<TravelNotes, Long> {
}
