package com.intuit.commentsService.exception;


public class CommentsServiceException extends RuntimeException{

    public CommentsServiceException(String message) {
        super(message);
    }

    public CommentsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
