package com.reviewblog.demo.service;

import com.reviewblog.demo.dto.PostRequest;
import com.reviewblog.demo.dto.PostResponse;
import com.reviewblog.demo.entity.Category;
import com.reviewblog.demo.entity.ContentType;
import com.reviewblog.demo.entity.*;
import com.reviewblog.demo.exception.ResourceNotFoundException;
import com.reviewblog.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;

    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(ContentType type, Long categoryId, String keyword, Pageable pageable) {
        Page<Post> posts = (keyword != null && !keyword.isBlank())
                ? postRepository.searchByTitle(keyword, pageable)
                : postRepository.filterPosts(type, categoryId, pageable);
        return  posts.map(this::toResponse);
    }

    @Transactional
    public PostResponse getBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(()-> new ResourceNotFoundException("Khong tim thay bai viet:"+ slug));
        post.setViewCount(post.getViewCount()+1);
        postRepository.save(post);
        return toResponse(post);
    }
    @Transactional
    public PostResponse create(PostRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Khong tim thay nguoi dung:"+ username));

        Category category = null;
        if(request.getCategoryId() != null){
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(()-> new ResourceNotFoundException("Khong tim thay danh muc id="+request.getCategoryId()));
        }
        Post post = Post.builder()
                .title(request.getTitle())
                .slug(generateUniqueSlug(request.getTitle()))
                .content(request.getContent())
                .summary(request.getSummary())
                .coverImageUrl(request.getCoverImageUrl())
                .type(request.getType())
                .category(category)
                .author(author)
                .tags(tagService.findOrCreateTags(request.getTagNames()))
                .build();

        return toResponse(postRepository.save(post));

    }

    @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Khong tim thay bai viet id="+ id));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setCoverImageUrl(request.getCoverImageUrl());
        post.setType(request.getType());
        post.setTags(tagService.findOrCreateTags(request.getTagNames()));

        if(request.getCategoryId() != null){
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục id=" + request.getCategoryId()));
            post.setCategory(category);
        }

        return toResponse(postRepository.save(post));
    }

    @Transactional
    public void delete(Long id) {
        if(!postRepository.existsById(id)){
            throw new ResourceNotFoundException("Không tìm thấy bài viết id=" + id);
        }
        postRepository.deleteById(id);
    }


    private String generateUniqueSlug(String title) {
        String base = CategoryService.toSlug(title);
        String slug = base;
        int counter = 1;
        while (postRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + counter;
            counter++;
        }
        return slug;
    }
    private PostResponse toResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .content(post.getContent())
                .summary(post.getSummary())
                .coverImageUrl(post.getCoverImageUrl())
                .type(post.getType().name())
                .averageRating(post.getAverageRating())
                .viewCount(post.getViewCount())
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .authorName(post.getAuthor().getDisplayName())
                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .build();
    }
}
