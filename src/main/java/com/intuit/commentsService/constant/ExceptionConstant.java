package com.intuit.commentsService.constant;

public interface ExceptionConstant {

    String COMMENT_NOT_FOUND = "Comment not found with id: %s";
    String PARENT_COMMENT_NOT_FOUND = "Parent Comment not found with ParentId: %s";
    String COMMENT_SAVE_ERROR = "Error saving comment";
    String COMMENT_ADD_ERROR = "Error adding comment";
    String COMMENT_LIKE_ERROR = "Error liking comment";
    String ADD_REPLY_ERROR = "Error adding reply to comment :%s";
    String COMMENT_DISLIKE_ERROR = "Error disLiking comment";
    String LIKE_RETRIEVE_ERROR ="Error retrieving likes";
    String DISLIKE_RETRIEVE_ERROR ="Error retrieving disLikes";
    String REPLY_RETRIEVE_ERROR = "Error getting replies to comment %s";
    String FIRST_LEVEL_COMMENT_RETRIEVE_ERROR = "Error getting FirstLevelComments to post: %s";
    String EXCEEDED_REQUESTS_PER_SECOND = "Exceeded the limit of requests, Please retry in some time";
}