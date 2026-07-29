package com.reviewblog.demo.controller;


import com.reviewblog.demo.dto.CategoryRequest;
import com.reviewblog.demo.entity.Category;
import com.reviewblog.demo.entity.ContentType;
import com.reviewblog.demo.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<List<Category>> getAll(@RequestParam(required = false) ContentType type) {
        List<Category> categories = (type != null) ? categoryService.getByType(type) : categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
