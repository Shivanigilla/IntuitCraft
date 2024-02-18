package com.intuit.commentsService.util;

import com.intuit.commentsService.constant.MockConstant;
import com.intuit.commentsService.model.Comment;

public class ExpectedUtil {
    public static final Comment expectedComment = Comment.builder()
            .userId(MockConstant.USER1)
            .content(MockConstant.COMMENT_CONTENT)
            .postId(MockConstant.POST_ID)
            .commentId(MockConstant.GENERATED_COMMENT_ID)
            .build();

    public static final Comment expectedReply = Comment.builder()
            .userId(MockConstant.USER1)
            .content(MockConstant.COMMENT_CONTENT)
            .postId(MockConstant.POST_ID)
            .commentId(MockConstant.GENERATED_COMMENT_ID)
            .build();
}
