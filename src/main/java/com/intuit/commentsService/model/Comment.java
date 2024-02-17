package com.intuit.commentsService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {
    @Id
    private String commentId;
    private String userId;
    private String postId;
    private String parentCommentId;
    private String content;
    private Long timestamp;
    @Builder.Default
    private Set<LikeDislike> likes=new HashSet<>();
    @Builder.Default
    private Set<LikeDislike> dislikes=new HashSet<>();
    @Builder.Default
    private Set<Comment> replies=new HashSet<>();
}
