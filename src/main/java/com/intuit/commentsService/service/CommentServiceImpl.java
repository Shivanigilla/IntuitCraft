package com.intuit.commentsService.service;

import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.model.LikeDisLikeType;
import com.intuit.commentsService.model.LikeDislike;
import com.intuit.commentsService.repository.CommentRepository;
import com.intuit.commentsService.util.TimeUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.intuit.commentsService.util.CommentUtility.generateId;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(String userId, String content, String postId) {
        // Create a new comment with a generated commentId and timestamp
        Comment comment = Comment.builder()
                        .userId(userId)
                                .content(content)
                                        .postId(postId)
                                                .build();

        comment.setCommentId(generateId());
        comment.setTimestamp(TimeUtility.getCurrentTimeStamp());
        // Save the comment to the database
        commentRepository.save(comment);

        return comment;
    }

    @Override
    public Comment addReply(String parentCommentId, String userId, String content) {
        // Create a new reply with a generated commentId and timestamp
        Comment reply = Comment.builder().userId(userId).parentCommentId(parentCommentId).content(content).build(); // Assuming replies have no postId
        reply.setCommentId(generateId());
        reply.setTimestamp(TimeUtility.getCurrentTimeStamp());


        // Save the reply to the database
        commentRepository.save(reply);

        // Associate the reply with the parent comment
        Comment parentComment = commentRepository.findById(parentCommentId).orElse(null);
        if (parentComment != null) {
            parentComment.getReplies().add(reply);
            commentRepository.save(parentComment);
        }

        return reply;
    }

    @Override
    public void likeComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {

            if (hasUserDislikedComment(comment, userId)) {
                // Optionally handle the case where the user has disliked the comment
                // For example, you might want to remove the dislike before allowing the like
                removeDislikeFromComment(comment, userId);
            }
                addLikeToComment(comment, userId);

            commentRepository.save(comment);
        }
    }

    @Override
    public void dislikeComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            if (hasUserLikedComment(comment, userId)) {
                // Optionally handle the case where the user has liked the comment
                // For example, you might want to remove the like before allowing the dislike
                removeLikeFromComment(comment, userId);
            }

            addDisLikeToComment(comment, userId);
            commentRepository.save(comment);
        }
    }

    @Override
    public List<Comment> getReplies(String commentId, int n) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        // Fetch replies for a specific comment, limiting to 'n'
        return commentRepository.findByParentCommentId(commentId,sort).stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> getFirstLevelComments(String postId, int n) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp"); // Sort comments by timestamp in descending order
        return commentRepository.findByPostIdAndParentCommentIdIsNull(postId, sort).stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    private boolean hasUserDislikedComment(Comment comment, String userId) {
        return comment.getDislikes().stream().anyMatch(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    private void removeDislikeFromComment(Comment comment, String userId) {
        comment.getDislikes().removeIf(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    private void addLikeToComment(Comment comment, String userId) {
        LikeDislike like = new LikeDislike(userId, LikeDisLikeType.LIKE);
        comment.getLikes().add(like);
    }

    private boolean hasUserLikedComment(Comment comment, String userId) {
        return comment.getLikes().stream().anyMatch(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    private void removeLikeFromComment(Comment comment, String userId) {
        comment.getLikes().removeIf(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    private void addDisLikeToComment(Comment comment, String userId) {
        LikeDislike disLike = new LikeDislike(userId, LikeDisLikeType.DISLIKE);
        comment.getDislikes().add(disLike);
    }
}
