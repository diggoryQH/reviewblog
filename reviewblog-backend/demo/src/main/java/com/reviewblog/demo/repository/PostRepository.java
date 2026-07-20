package com.reviewblog.demo.repository;

import com.reviewblog.demo.entity.ContentType;
import com.reviewblog.demo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    Page<Post> findByType(ContentType type, Pageable pageable);

    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Post> filterPosts(@Param("type") ContentType type,
                           @Param("categoryId") Long categoryId,
                           Pageable pageable);
}
