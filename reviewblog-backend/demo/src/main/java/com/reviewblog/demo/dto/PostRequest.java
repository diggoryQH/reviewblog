package com.reviewblog.demo.dto;


import com.reviewblog.demo.entity.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private String summary;
    private String coverImageUrl;

    @NotNull
    private ContentType type;

    private Long categoryId;

    private List<String> tagNames;

}
