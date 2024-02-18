package com.intuit.commentsService.util;

import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.model.LikeDisLikeType;
import com.intuit.commentsService.model.LikeDislike;

import java.util.Arrays;
import java.util.Set;

import static com.intuit.commentsService.constant.MockConstant.*;

public class MockUtil {

    public static final Comment parentComment = Comment.builder()
            .commentId(PARENT_COMMENT_ID)
            .userId(PARENT_USER)
            .content(COMMENT_CONTENT)
            .build();
    public static final Set<LikeDislike> likeUsers = Set.of(
            new LikeDislike(USER1,LikeDisLikeType.LIKE),
            new LikeDislike(USER2,LikeDisLikeType.LIKE)
    );

    public static final Set<LikeDislike> disLikeUsers = Set.of(
            new LikeDislike(USER1,LikeDisLikeType.DISLIKE),
            new LikeDislike(USER2,LikeDisLikeType.DISLIKE)
    );


}
