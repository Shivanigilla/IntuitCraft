package com.intuit.commentsService.rest.controller;

import com.intuit.commentsService.DTO.CommentRequest;
import com.intuit.commentsService.DTO.ReplyRequest;
import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.service.CommentService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.intuit.commentsService.constant.ExceptionConstant.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
//@EnableOpenApi
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/addComment")
    public ResponseEntity<Object> addComment(@RequestBody CommentRequest commentRequest) {
        try {
            Comment comment = commentService.addComment(commentRequest.getUserId(), commentRequest.getContent(), commentRequest.getPostId());
            return ResponseEntity.ok(comment);
        }catch (Exception e){
            log.info(COMMENT_SAVE_ERROR,e);
            return ResponseEntity.internalServerError().body(COMMENT_SAVE_ERROR);
        }

    }

    @PostMapping("/addReply")
    public ResponseEntity<Object> addReply(@RequestBody ReplyRequest replyRequest) {
        try {
            Comment reply = commentService.addReply(replyRequest.getParentCommentId(), replyRequest.getUserId(), replyRequest.getContent());
            return ResponseEntity.ok(reply);
        } catch (Exception e) {
            log.info(String.format(ADD_REPLY_ERROR,replyRequest.getParentCommentId()), e);
            return ResponseEntity.internalServerError().body(String.format(ADD_REPLY_ERROR,replyRequest.getParentCommentId()));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getFirstLevelComments(@RequestParam String postId,@RequestParam(defaultValue = "5") int n) {
        try {
            List<Comment> comments = commentService.getFirstLevelComments(postId, n);
            return ResponseEntity.ok(comments);
        }catch (Exception e) {
            log.info(String.format(FIRST_LEVEL_COMMENT_RETRIEVE_ERROR,postId), e);
            return ResponseEntity.internalServerError().body(String.format(FIRST_LEVEL_COMMENT_RETRIEVE_ERROR,postId));
        }
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<Object> getReplies(@PathVariable @NotBlank String commentId, @RequestParam(defaultValue = "5") int n) {
        try {
            List<Comment> comments = commentService.getReplies(commentId, n);
            return ResponseEntity.ok(comments);
        }catch (Exception e) {
            log.info(String.format(REPLY_RETRIEVE_ERROR,commentId), e);
            return ResponseEntity.internalServerError().body(String.format(REPLY_RETRIEVE_ERROR,commentId));
        }
    }

    @PostMapping("/{commentId}/like")
    public void likeComment(@PathVariable String commentId, @RequestBody String userId) {
        try {
            commentService.likeComment(commentId, userId);
        }catch (Exception e) {
            log.info(COMMENT_LIKE_ERROR, e);
        }
    }

    @PostMapping("/{commentId}/dislike")
    public void dislikeComment(@PathVariable String commentId, @RequestBody String userId) {
        try {
            commentService.dislikeComment(commentId, userId);
        }catch (Exception e) {
            log.info(COMMENT_DISLIKE_ERROR, e);
        }
    }


}
