package com.intuit.commentsService.service;

import com.intuit.commentsService.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(String userId, String content, String postId);
    Comment addReply(String parentCommentId, String userId, String content);
    void likeComment(String commentId, String userId);
    void dislikeComment(String commentId, String userId);
    List<Comment> getReplies(String commentId, int page, int pageSize);
    List<Comment> getFirstLevelComments(String postId, int page, int pageSize);

    List<String> getLikes(String commentId);
    List<String > getDislikes(String commentId);
}
