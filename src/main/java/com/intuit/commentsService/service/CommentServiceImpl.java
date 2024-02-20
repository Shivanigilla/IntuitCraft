package com.intuit.commentsService.service;

import com.intuit.commentsService.exception.CommentNotFoundException;
import com.intuit.commentsService.exception.CommentsServiceException;
import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.model.LikeDislike;
import com.intuit.commentsService.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.intuit.commentsService.constant.CommentServiceConstant.TIME_STAMP;
import static com.intuit.commentsService.constant.ExceptionConstant.*;
import static com.intuit.commentsService.util.CommentUtility.generateId;
import static com.intuit.commentsService.util.CommentUtility.*;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(String userId, String content, String postId) {
        try {
            // Create a new comment with a generated commentId and timestamp
            Comment comment = Comment.builder()
                    .userId(userId)
                    .content(content)
                    .postId(postId)
                    .commentId(generateId())
                    .build();
            // Save the comment to the database
            commentRepository.save(comment);

            return comment;
        } catch (Exception e) {
            throw new CommentsServiceException(COMMENT_SAVE_ERROR, e);
        }
    }

    @Override
    public Comment addReply(String parentCommentId, String userId, String content) {
        try {
            Comment reply = Comment.builder()
                    .userId(userId)
                    .parentCommentId(parentCommentId)
                    .content(content)
                    .commentId(generateId())
                    .build(); // Assuming replies have no postId

            commentRepository.save(reply);

            // Associate the reply with the parent comment
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new CommentNotFoundException(String.format(PARENT_COMMENT_NOT_FOUND,parentCommentId)));

            if (parentComment != null) {
                parentComment.getReplies().add(reply);
                commentRepository.save(parentComment);
            }
            return reply;
        } catch (Exception e) {
            throw new CommentsServiceException(String.format(ADD_REPLY_ERROR,parentCommentId), e);
        }
    }

    @Override
    public void likeComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(() -> new CommentNotFoundException(String.format(COMMENT_NOT_FOUND,commentId)));

        try {
            if (comment != null) {
                if (hasUserDislikedComment(comment, userId)) {
                    // Optionally handle the case where the user has disliked the comment
                    // For example, you might want to remove the dislike before allowing the like
                    removeDislikeFromComment(comment, userId);
                }
                addLikeToComment(comment, userId);
                commentRepository.save(comment);
            }
        } catch (Exception e) {
            throw new CommentsServiceException(COMMENT_LIKE_ERROR, e);
        }
    }

    @Override
    public void dislikeComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(String.format(COMMENT_NOT_FOUND,commentId)));
        try {
            if (comment != null) {
                if (hasUserLikedComment(comment, userId)) {
                    // Optionally handle the case where the user has liked the comment
                    // For example, you might want to remove the like before allowing the dislike
                    removeLikeFromComment(comment, userId);
                }
                addDisLikeToComment(comment, userId);
                commentRepository.save(comment);
            }
        } catch (Exception e) {
            throw new CommentsServiceException(COMMENT_DISLIKE_ERROR, e);
        }
    }

    @Override
    public List<Comment> getReplies(String commentId, int page, int pageSize) {
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, TIME_STAMP); // Sort comments by timestamp in descending order
            Pageable pageable = PageRequest.of(page, pageSize, sort);
            // Fetch replies for a specific comment, limiting to 'n'
            return commentRepository.findByParentCommentId(commentId, pageable).stream()
                    .limit(pageSize)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CommentsServiceException( String.format(REPLY_RETRIEVE_ERROR,commentId), e);
        }
    }

    @Override
    public List<Comment> getFirstLevelComments(String postId, int page, int pageSize) {
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, TIME_STAMP); // Sort comments by timestamp in descending order
            Pageable pageable = PageRequest.of(page, pageSize, sort);

            return commentRepository.findByPostIdAndParentCommentIdIsNull(postId, pageable).stream()
                    .limit(pageSize)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CommentsServiceException(String.format(FIRST_LEVEL_COMMENT_RETRIEVE_ERROR,postId), e);
        }
    }

    @Override
    public List<String> getLikes(String commentId) {
        try {
            Optional<Comment> optionalComment = commentRepository.findById(commentId);
            if (optionalComment.isPresent()) {
                return optionalComment.get().getLikes().stream().map(LikeDislike::getUserId).collect(Collectors.toList());
            } else {
                throw new CommentNotFoundException(String.format(COMMENT_NOT_FOUND,commentId));
            }
        } catch (Exception e) {
            throw new CommentsServiceException(LIKE_RETRIEVE_ERROR, e);
        }
    }

    @Override
    public List<String> getDislikes(String commentId) {
        try {
            Optional<Comment> optionalComment = commentRepository.findById(commentId);
            if (optionalComment.isPresent()) {
                return optionalComment.get().getDislikes().stream().map(LikeDislike::getUserId).collect(Collectors.toList());
            } else {
                throw new CommentNotFoundException(String.format(COMMENT_NOT_FOUND,commentId));
            }
        } catch (Exception e) {
            throw new CommentsServiceException(DISLIKE_RETRIEVE_ERROR, e);
        }
    }


}
