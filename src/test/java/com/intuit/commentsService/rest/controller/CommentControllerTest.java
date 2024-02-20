package com.intuit.commentsService.rest.controller;

import com.intuit.commentsService.DTO.CommentRequest;
import com.intuit.commentsService.DTO.ReplyRequest;
import com.intuit.commentsService.exception.CommentNotFoundException;
import com.intuit.commentsService.exception.CommentsServiceException;
import com.intuit.commentsService.model.Comment;
import com.intuit.commentsService.model.LikeDisLikeType;
import com.intuit.commentsService.model.LikeDislike;
import com.intuit.commentsService.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static com.intuit.commentsService.constant.ExceptionConstant.*;
import static com.intuit.commentsService.constant.MockConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Test
    public void addComment_Success() {
        // Arrange
        CommentRequest commentRequest = CommentRequest.builder().userId(USER1).content(COMMENT_CONTENT).postId(POST_ID).build();
        Comment savedComment = Comment.builder().build();

        when(commentService.addComment(eq(commentRequest.getUserId()), eq(commentRequest.getContent()), eq(commentRequest.getPostId()))).thenReturn(savedComment);

        // Act
        ResponseEntity<Object> response = commentController.addComment(commentRequest);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(savedComment, response.getBody());
    }


    @Test
    public void addComment_Exception() {
        // Arrange
        CommentRequest commentRequest = CommentRequest.builder().userId(USER1).content(COMMENT_CONTENT).postId(POST_ID).build();

        when(commentService.addComment(eq(commentRequest.getUserId()), eq(commentRequest.getContent()), eq(commentRequest.getPostId())))
                .thenThrow(new CommentsServiceException(COMMENT_SAVE_ERROR));

        // Act
        ResponseEntity<Object> response = commentController.addComment(commentRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(COMMENT_SAVE_ERROR, response.getBody());
    }

    @Test
    public void addReply_Success() {
        // Arrange
        ReplyRequest replyRequest = ReplyRequest.builder().userId(USER1).parentCommentId(PARENT_COMMENT_ID).content(COMMENT_CONTENT).build();
        Comment savedReply = Comment.builder().build();

        when(commentService.addReply(eq(replyRequest.getParentCommentId()), eq(replyRequest.getUserId()), eq(replyRequest.getContent())))
                .thenReturn(savedReply);

        // Act
        ResponseEntity<Object> response = commentController.addReply(replyRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedReply, response.getBody());
    }

    @Test
    public void addReply_Exception() {
        // Arrange
        ReplyRequest replyRequest = ReplyRequest.builder().userId(USER1).parentCommentId(PARENT_COMMENT_ID).content(COMMENT_CONTENT).build();

        when(commentService.addReply(eq(replyRequest.getParentCommentId()), eq(replyRequest.getUserId()), eq(replyRequest.getContent())))
                .thenThrow(new CommentsServiceException(String.format(ADD_REPLY_ERROR, PARENT_COMMENT_ID)));

        // Act
        ResponseEntity<Object> response = commentController.addReply(replyRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(String.format(ADD_REPLY_ERROR, PARENT_COMMENT_ID), response.getBody());
    }

    @Test
    public void getFirstLevelComments_Success() {
        // Arrange
        String postId = POST_ID;
        int pageSize = 5;
        int page = 0;
        List<Comment> firstLevelComments = Arrays.asList(Comment.builder().build(), Comment.builder().build());

        when(commentService.getFirstLevelComments(eq(postId), eq(page), eq(pageSize))).thenReturn(firstLevelComments);

        // Act
        ResponseEntity<Object> response = commentController.getFirstLevelComments(postId, page, pageSize);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(firstLevelComments, response.getBody());
    }

    @Test
    public void getFirstLevelComments_Exception() {
        // Arrange
        String postId = POST_ID;
        int pageSize = 5;
        int page = 0;

        when(commentService.getFirstLevelComments(eq(postId), eq(page), eq(pageSize)))
                .thenThrow(new CommentsServiceException(String.format(FIRST_LEVEL_COMMENT_RETRIEVE_ERROR, postId)));

        // Act
        ResponseEntity<Object> response = commentController.getFirstLevelComments(postId, page, pageSize);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(String.format(FIRST_LEVEL_COMMENT_RETRIEVE_ERROR, postId), response.getBody());
    }


    @Test
    public void getReplies_Success() {
        // Arrange
        String commentId = COMMENT_ID;
        int pageSize = 5;
        int page = 0;
        List<Comment> replies = Arrays.asList(Comment.builder().build(), Comment.builder().build());


        when(commentService.getReplies(eq(commentId), eq(page), eq(pageSize))).thenReturn(replies);

        // Act
        ResponseEntity<Object> response = commentController.getReplies(commentId, page,pageSize);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(replies, response.getBody());
    }

    @Test
    public void getReplies_Exception() {
        // Arrange
        String commentId = COMMENT_ID;
        int pageSize = 5;
        int page = 0;

        when(commentService.getReplies(eq(commentId), eq(page), eq(pageSize)))
                .thenThrow(new CommentsServiceException(String.format(REPLY_RETRIEVE_ERROR, commentId)));

        // Act
        ResponseEntity<Object> response = commentController.getReplies(commentId, page, pageSize);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(String.format(REPLY_RETRIEVE_ERROR, commentId), response.getBody());
    }

    @Test
    public void likeComment_Success() {
        // Arrange
        String commentId = COMMENT_ID;
        String userId = USER1;

        // Act
        commentController.likeComment(commentId, userId);

        // Assert
        verify(commentService, times(1)).likeComment(eq(commentId), eq(userId));
    }

    @Test
    public void likeComment_Exception() {
        // Arrange
        String commentId = COMMENT_ID;
        String userId = USER1;

        doThrow(new CommentsServiceException(COMMENT_LIKE_ERROR))
                .when(commentService).likeComment(eq(commentId), eq(userId));

        // Act
        assertThrows(CommentsServiceException.class, () -> commentController.likeComment(commentId, userId));

    }

    @Test
    public void dislikeComment_Success() {
        // Arrange
        String commentId = COMMENT_ID;
        String userId = USER1;

        // Act
        commentController.dislikeComment(commentId, userId);

        // Assert
        verify(commentService, times(1)).dislikeComment(eq(commentId), eq(userId));
    }

    @Test
    public void dislikeComment_Exception() {
        // Arrange
        String commentId = COMMENT_ID;
        String userId = USER1;

        doThrow(new CommentsServiceException(COMMENT_DISLIKE_ERROR))
                .when(commentService).dislikeComment(eq(commentId), eq(userId));

        // Act
        assertThrows(CommentsServiceException.class, () -> commentController.dislikeComment(commentId, userId));

    }


    @Test
    void getLikes_Success() {
        // Arrange
        String commentId = COMMENT_ID;
        when(commentService.getLikes(commentId)).thenReturn(List.of(USER1, USER2));

        // Act
        ResponseEntity<Object> responseEntity = commentController.getLikes(commentId);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof List);
        List<String> likes = (List<String>) responseEntity.getBody();
        assertEquals(2, likes.size());
        assertTrue(likes.contains(USER1));
        assertTrue(likes.contains(USER2));

        // Verify
        verify(commentService, times(1)).getLikes(commentId);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    void getLikes_CommentNotFound() {
        // Arrange
        String commentId = COMMENT_ID;
        when(commentService.getLikes(commentId)).thenThrow(new CommentNotFoundException(LIKE_RETRIEVE_ERROR));

        // Act
        ResponseEntity<Object> responseEntity = commentController.getLikes(commentId);

        // Assert
        assertEquals(500, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(LIKE_RETRIEVE_ERROR, responseEntity.getBody());

        // Verify
        verify(commentService, times(1)).getLikes(commentId);
        verifyNoMoreInteractions(commentService);
    }


    @Test
    void getDisLikes_Success() {
        // Arrange
        String commentId = COMMENT_ID;
        when(commentService.getDislikes(commentId)).thenReturn(List.of(USER1, USER2));

        // Act
        ResponseEntity<Object> responseEntity = commentController.getDisLikes(commentId);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof List);
        List<String> disLikes = (List<String>) responseEntity.getBody();
        assertEquals(2, disLikes.size());
        assertTrue(disLikes.contains(USER1));
        assertTrue(disLikes.contains(USER2));

        // Verify
        verify(commentService, times(1)).getDislikes(commentId);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    void getDisLikes_CommentNotFound() {
        // Arrange
        String commentId = COMMENT_ID;
        when(commentService.getDislikes(commentId)).thenThrow(new CommentNotFoundException(DISLIKE_RETRIEVE_ERROR));

        // Act
        ResponseEntity<Object> responseEntity = commentController.getDisLikes(commentId);

        // Assert
        assertEquals(500, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(DISLIKE_RETRIEVE_ERROR, responseEntity.getBody());

        // Verify
        verify(commentService, times(1)).getDislikes(commentId);
        verifyNoMoreInteractions(commentService);
    }

}
