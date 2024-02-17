package com.intuit.commentsService.util;

import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.model.LikeDisLikeType;
import com.intuit.commentsService.model.LikeDislike;

import java.util.UUID;

public class CommentUtility {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static boolean hasUserDislikedComment(Comment comment, String userId) {
        return comment.getDislikes().stream().anyMatch(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    public static void removeDislikeFromComment(Comment comment, String userId) {
        comment.getDislikes().removeIf(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    public static void addLikeToComment(Comment comment, String userId) {
        LikeDislike like = new LikeDislike(userId, LikeDisLikeType.LIKE);
        comment.getLikes().add(like);
    }

    public static boolean hasUserLikedComment(Comment comment, String userId) {
        return comment.getLikes().stream().anyMatch(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    public static void removeLikeFromComment(Comment comment, String userId) {
        comment.getLikes().removeIf(likeDislike -> likeDislike.getUserId().equals(userId));
    }

    public static void addDisLikeToComment(Comment comment, String userId) {
        LikeDislike disLike = new LikeDislike(userId, LikeDisLikeType.DISLIKE);
        comment.getDislikes().add(disLike);
    }
}
