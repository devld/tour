package me.devld.tour.repository;

import me.devld.tour.entity.TourUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TourUserRepository extends CrudRepository<TourUser, Long> {

    Optional<TourUser> findByUsername(String username);

    Optional<TourUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
