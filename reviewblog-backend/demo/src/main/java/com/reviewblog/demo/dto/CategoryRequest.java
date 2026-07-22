package com.reviewblog.demo.dto;

import com.reviewblog.demo.entity.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank
    private String name;
    @NotNull
    private ContentType type;
}
