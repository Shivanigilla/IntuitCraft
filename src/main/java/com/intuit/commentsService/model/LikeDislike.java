package com.intuit.commentsService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LikeDislike {
    private String userId;
    private LikeDisLikeType likeDislikeType;
}