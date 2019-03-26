package me.devld.tour.repository;

import me.devld.tour.entity.TourUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourUserRepository extends JpaRepository<TourUser, Long> {

    Optional<TourUser> findByUsername(String username);

    Optional<TourUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
