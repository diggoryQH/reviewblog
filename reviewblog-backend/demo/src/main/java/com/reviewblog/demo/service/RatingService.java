package com.reviewblog.demo.service;


import com.reviewblog.demo.entity.Post;
import com.reviewblog.demo.entity.Rating;
import com.reviewblog.demo.entity.User;
import com.reviewblog.demo.exception.ResourceNotFoundException;
import com.reviewblog.demo.repository.PostRepository;
import com.reviewblog.demo.repository.RatingRepository;
import com.reviewblog.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public double rate(Long postId, Integer stars, String username){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy bài viết id=" + postId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy user: " + username));

        Rating rating = ratingRepository.findByPostIdAndUserId(postId, user.getId())
                .orElse(Rating.builder().post(post).user(user).build());
        rating.setStars(stars);
        ratingRepository.save(rating);

        Double avg = ratingRepository.findAverageStarsByPostId(postId);
        post.setAverageRating(avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0);
        postRepository.save(post);

        return post.getAverageRating();
    }
}
