package com.intuit.commentsService.util;

import java.util.UUID;

public class CommentUtility {
    public static String generateId() {
        // Generate a unique commentId, for example using UUID
        return UUID.randomUUID().toString();
    }
}
