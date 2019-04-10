package me.devld.tour.repository;

import me.devld.tour.entity.SpotTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotTicketRepository extends JpaRepository<SpotTicket, Long> {
}
