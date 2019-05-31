package me.devld.tour.repository;

import me.devld.tour.entity.SpotTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotTicketRepository extends CrudRepository<SpotTicket, Long> {

    List<SpotTicket> findAllBySpotId(long spotId);

    int deleteAllBySpotId(long spotId);

}
