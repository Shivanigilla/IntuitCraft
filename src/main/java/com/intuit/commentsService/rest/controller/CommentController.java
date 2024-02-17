package com.intuit.commentsService.rest.controller;

import com.intuit.commentsService.DTO.CommentRequest;
import com.intuit.commentsService.DTO.ReplyRequest;
import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
//@EnableSwagger2
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/addComment")
    public ResponseEntity<Comment> addComment(@RequestBody CommentRequest commentRequest) {
        Comment comment = commentService.addComment(commentRequest.getUserId(), commentRequest.getContent(), commentRequest.getPostId());
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/addReply")
    public ResponseEntity<Comment> addReply(@RequestBody ReplyRequest replyRequest) {
        Comment reply = commentService.addReply(replyRequest.getParentCommentId(),replyRequest.getUserId(), replyRequest.getContent());
        return ResponseEntity.ok(reply);
    }

    @GetMapping
    public List<Comment> getFirstLevelComments(@RequestParam String postId,@RequestParam(defaultValue = "5") int n) {
        return commentService.getFirstLevelComments(postId,n);
    }

    @GetMapping("/{commentId}/replies")
    public List<Comment> getReplies(@PathVariable String commentId,@RequestParam(defaultValue = "5") int n) {
        return commentService.getReplies(commentId,n);
    }

    @PostMapping("/{commentId}/like")
    public void likeComment(@PathVariable String commentId, @RequestBody String userId) {
        commentService.likeComment(commentId, userId);
    }

    @PostMapping("/{commentId}/dislike")
    public void dislikeComment(@PathVariable String commentId, @RequestBody String userId) {
        commentService.dislikeComment(commentId, userId);
    }


}
