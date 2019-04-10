package me.devld.tour.repository;

import me.devld.tour.entity.SpotComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotCommentRepository extends JpaRepository<SpotComment, Long> {
}
