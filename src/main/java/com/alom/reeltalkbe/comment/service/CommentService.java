package com.alom.reeltalkbe.comment.service;


import com.alom.reeltalkbe.comment.dto.CommentResponseDTO;
import com.alom.reeltalkbe.comment.dto.CommentRequestDTO;
import com.alom.reeltalkbe.comment.entity.Comment;
import com.alom.reeltalkbe.comment.repository.CommentRepository;
import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private UserRepository userRepsoitory;

    private ReviewRepository reviewRepository;

    private CommentRepository commentRepository;

    public CommentResponseDTO add(Long userId, Long reviewId, CommentRequestDTO commentParamDTO){

        User user = userRepsoitory.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new  BaseException(BaseResponseStatus.INVALID_REVIEW));


        Comment comment = Comment.builder()
                .user(user)
                .review(review)
                .content(commentParamDTO.getContent())
                .rating(commentParamDTO.getRating())
                .build();

        return new CommentResponseDTO(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getByReview(long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW));

        List<Comment> comments = commentRepository.findByReview(review);


        return (List<CommentResponseDTO>) comments.stream()
                .map(CommentResponseDTO::new)
                .collect(Collectors.toList());

    }

    public CommentResponseDTO update(Long userId, Long commentId, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        User user = userRepsoitory.findById(userId)
                .orElseThrow(() -> new  BaseException(BaseResponseStatus.NON_EXIST_USER));

        if (comment.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER);
        }

        comment.update(commentRequestDTO.getContent(), commentRequestDTO.getRating());
        return new CommentResponseDTO(commentRepository.save(comment));

    }

    public void delete(Long userId, Long commentId){
        User user = userRepsoitory.findById(userId)
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        if (comment.getUser().getUserId().equals(user.getUserId())) {
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER);
        }
        commentRepository.deleteById(commentId);
    }

}
