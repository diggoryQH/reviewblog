package com.reviewblog.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImageUrl;
    private String type;
    private Double averageRating;
    private Long viewCount;
    private String categoryName;
    private String authorName;
    private List<String> tags;
    private LocalDateTime createdAt;
}
