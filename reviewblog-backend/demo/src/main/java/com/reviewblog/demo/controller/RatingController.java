package com.reviewblog.demo.controller;


import com.reviewblog.demo.dto.RatingRequest;
import com.reviewblog.demo.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/api/posts/{postId}/ratings")
    public ResponseEntity<Map<String, Double>> rate(
            @PathVariable Long postId, @Valid @RequestBody RatingRequest request, Authentication auth) {
        double avg = ratingService.rate(postId, request.getStars(), auth.getName());
        return ResponseEntity.ok(Map.of("averageRating", avg));
    }
}
