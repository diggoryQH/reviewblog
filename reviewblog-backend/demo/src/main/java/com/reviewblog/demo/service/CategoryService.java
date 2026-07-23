package com.reviewblog.demo.service;

import com.reviewblog.demo.dto.CategoryRequest;
import com.reviewblog.demo.entity.Category;
import com.reviewblog.demo.entity.ContentType;
import com.reviewblog.demo.exception.DuplicateResourceException;
import com.reviewblog.demo.exception.ResourceNotFoundException;
import com.reviewblog.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public List<Category> getByType(ContentType type) {
        return categoryRepository.findByType(type);
    }

    public Category create(CategoryRequest request) {
        String slug = toSlug(request.getName());
        if (categoryRepository.findBySlug(slug).isPresent()) {
            throw new DuplicateResourceException("Danh mục đã tồn tại: " + request.getName());
        }
        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .type(request.getType())
                .build();
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy danh mục id=" + id);
        }
        categoryRepository.deleteById(id);
    }
    public static String toSlug(String input) {
        String noAccent = Normalizer.normalize(input, Normalizer.Form.NFD);
        noAccent = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(noAccent).replaceAll("");
        noAccent = noAccent.replace('đ', 'd').replace('Đ', 'D');
        return noAccent.toLowerCase().trim().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");
    }
}