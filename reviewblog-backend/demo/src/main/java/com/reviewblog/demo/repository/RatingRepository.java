package com.reviewblog.demo.repository;

import com.reviewblog.demo.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByPostIdAndUserId(Long postId, Long userId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.post.id = :postId")
    Double findAverageStarsByPostId(@Param("postId") Long postId);
}
