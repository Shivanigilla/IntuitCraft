package com.intuit.commentsService.rest.controller;

import com.intuit.commentsService.DTO.CommentRequest;
import com.intuit.commentsService.DTO.ReplyRequest;
import com.intuit.commentsService.exception.CommentsServiceException;
import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.intuit.commentsService.constant.ExceptionConstant.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/addComment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody  CommentRequest commentRequest) {
        try {
            Comment comment = commentService.addComment(commentRequest.getUserId(), commentRequest.getContent(), commentRequest.getPostId());
            return ResponseEntity.ok(comment);
        }catch (Exception e){
            log.info(COMMENT_SAVE_ERROR,e);
            return ResponseEntity.internalServerError().body(COMMENT_SAVE_ERROR);
        }

    }

    @PostMapping("/addReply")
    public ResponseEntity<Object> addReply(@Valid @RequestBody ReplyRequest replyRequest) {
        try {
            Comment reply = commentService.addReply(replyRequest.getParentCommentId(), replyRequest.getUserId(), replyRequest.getContent());
            return ResponseEntity.ok(reply);
        } catch (Exception e) {
            log.info(String.format(ADD_REPLY_ERROR,replyRequest.getParentCommentId()), e);
            return ResponseEntity.internalServerError().body(String.format(ADD_REPLY_ERROR,replyRequest.getParentCommentId()));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getFirstLevelComments(@RequestParam String postId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int pageSize) {
        try {
            List<Comment> comments = commentService.getFirstLevelComments(postId, page, pageSize);
            return ResponseEntity.ok(comments);
        }catch (Exception e) {
            log.info(String.format(FIRST_LEVEL_COMMENT_RETRIEVE_ERROR,postId), e);
            return ResponseEntity.internalServerError().body(String.format(EXCEPTION_FORMAT,String.format(FIRST_LEVEL_COMMENT_RETRIEVE_ERROR,postId),e.getMessage()));
        }
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<Object> getReplies(@PathVariable @NotBlank String commentId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int pageSize) {
        try {
            List<Comment> comments = commentService.getReplies(commentId, page, pageSize);
            return ResponseEntity.ok(comments);
        }catch (Exception e) {
            log.info(String.format(REPLY_RETRIEVE_ERROR,commentId), e);
            return ResponseEntity.internalServerError().body(String.format(EXCEPTION_FORMAT,String.format(REPLY_RETRIEVE_ERROR,commentId),e.getMessage()));

        }
    }

    @PostMapping("/{commentId}/like")
    public void likeComment(@PathVariable String commentId, @RequestBody String userId) {
        try {
            commentService.likeComment(commentId, userId);
        }catch (Exception e) {
            log.info(COMMENT_LIKE_ERROR, e);
            throw new CommentsServiceException(COMMENT_LIKE_ERROR, e);

        }
    }

    @PostMapping("/{commentId}/dislike")
    public void dislikeComment(@PathVariable String commentId, @RequestBody String userId) {
        try {
            commentService.dislikeComment(commentId, userId);
        }catch (Exception e) {
            log.info(COMMENT_DISLIKE_ERROR, e);
            throw new CommentsServiceException(COMMENT_DISLIKE_ERROR, e);
        }
    }


    @GetMapping("/{commentId}/likedUsers")
    public ResponseEntity<Object> getLikes(@PathVariable @NotBlank String commentId) {
        try {
            List<String> likes = commentService.getLikes(commentId);
            return ResponseEntity.ok(likes);
        }  catch (Exception e) {
            log.error(LIKE_RETRIEVE_ERROR, e);
            return ResponseEntity.internalServerError().body(String.format(EXCEPTION_FORMAT,LIKE_RETRIEVE_ERROR,e.getMessage()));

        }
    }

    @GetMapping("/{commentId}/disLikedUsers")
    public ResponseEntity<Object> getDisLikes(@PathVariable @NotBlank String commentId) {
        try {
            List<String> dislikes = commentService.getDislikes(commentId);
            return ResponseEntity.ok(dislikes);
        }  catch (Exception e) {
            log.error(DISLIKE_RETRIEVE_ERROR, e);
            return ResponseEntity.internalServerError().body(String.format(EXCEPTION_FORMAT,DISLIKE_RETRIEVE_ERROR,e.getMessage()));
        }
    }

}