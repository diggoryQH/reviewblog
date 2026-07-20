package com.reviewblog.demo.repository;

import com.reviewblog.demo.entity.Category;
import com.reviewblog.demo.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    List<Category> findByType(ContentType type);
}
