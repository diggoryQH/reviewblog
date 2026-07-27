package com.reviewblog.demo.service;


import com.reviewblog.demo.dto.CommentRequest;
import com.reviewblog.demo.dto.CommentResponse;
import com.reviewblog.demo.entity.Comment;
import com.reviewblog.demo.entity.Post;
import com.reviewblog.demo.entity.User;
import com.reviewblog.demo.exception.ResourceNotFoundException;
import com.reviewblog.demo.repository.CommentRepository;
import com.reviewblog.demo.repository.PostRepository;
import com.reviewblog.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getByPost(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable).map(this::toResponse);
    }

    @Transactional
    public CommentResponse create(Long postId, CommentRequest request, String username){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy bài viết id=" + postId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));

        Comment comment = Comment.builder().content(request.getContent()).post(post).user(user).build();
        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public void delete(Long commentId, String username, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy bình luận id=" + commentId));

        if(!isAdmin && !comment.getUser().getUsername().equals(username)){
            throw new org.springframework.security.access.AccessDeniedException("Bạn không có quyền xóa bình luận này");
        }
        commentRepository.delete(comment);
    }
    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .userAvatar(comment.getUser().getAvatarUrl())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
