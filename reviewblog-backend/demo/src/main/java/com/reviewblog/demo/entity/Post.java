package com.reviewblog.demo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts",
        indexes = {
        @Index(name = "idx_post_slug", columnList = "slug", unique = true),
                @Index(name = "idx_post_category", columnList = "category_id"),
                @Index(name = "idx_post_created_at", columnList = "createdAt")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 200)
        private String title;

        @Column(nullable = false, unique = true, length = 220)
        private String slug;

        @Lob
        @Column(nullable = false)
        private String content;

        @Column(length = 500)
        private String summary;

        private String coverImageUrl;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ContentType type;

        @Builder.Default
        private Double averageRating = 0.0;

        @Builder.Default
        private Long viewCount = 0L;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        private Category category;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "author_id", nullable = false)
        private User author;

        @ManyToMany
        @JoinTable(
                name = "post_tags",
                joinColumns = @JoinColumn(name = "post_id"),
                inverseJoinColumns = @JoinColumn(name = "tag_id")
        )
        @Builder.Default
        private Set<Tag> tags = new HashSet<>();

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {
                this.createdAt = LocalDateTime.now();
                this.updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
                this.updatedAt = LocalDateTime.now();
        }
}
