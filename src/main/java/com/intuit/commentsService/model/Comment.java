package com.intuit.commentsService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.intuit.commentsService.util.TimeUtility;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@Builder
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {
    @Version
    @JsonIgnore
    private Long version;
    @Id
    private String commentId;
    private String userId;
    private String postId;
    private String parentCommentId;
    private String content;
    @Builder.Default
    private Long timestamp= TimeUtility.getCurrentTimeStamp();
    @Builder.Default
    @JsonInclude(NON_EMPTY)
    private Set<LikeDislike> likes=new HashSet<>();
    @Builder.Default
    @JsonInclude(NON_EMPTY)
    private Set<LikeDislike> dislikes=new HashSet<>();
    @Builder.Default
    @JsonInclude(NON_EMPTY)
    private Set<Comment> replies=new HashSet<>();
}

