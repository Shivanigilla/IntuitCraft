package com.intuit.commentsService.service;

import com.intuit.commentsService.exception.CommentNotFoundException;
import com.intuit.commentsService.exception.CommentsServiceException;
import com.intuit.commentsService.model.Comment;

import com.intuit.commentsService.repository.CommentRepository;
import com.intuit.commentsService.util.ExpectedUtil;
import com.intuit.commentsService.util.MockUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.intuit.commentsService.constant.MockConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addComment_Success() {


        Comment expectedComment = ExpectedUtil.expectedComment;

        when(commentRepository.save(any(Comment.class))).thenReturn(expectedComment);

        // Act
        Comment actualComment = commentService.addComment(USER1, COMMENT_CONTENT, POST_ID);

        // Assert
        assertNotNull(actualComment);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addComment_Exception() {
        when(commentRepository.save(any(Comment.class))).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.addComment(USER1, COMMENT_CONTENT, POST_ID);
        });

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addReply_Success() {
        // Arrange
        String parentCommentId = PARENT_COMMENT_ID;
        String userId = USER1;
        String content = COMMENT_CONTENT;

        Comment parentComment = Comment.builder()
                .commentId(parentCommentId)
                .commentId(COMMENT_ID)
                .userId(PARENT_USER)
                .content(COMMENT_CONTENT)
                .build();

        Comment expectedReply = Comment.builder()
                .userId(userId)
                .commentId(COMMENT_ID)
                .parentCommentId(parentCommentId)
                .content(content)
                .build();

        when(commentRepository.findById(eq(parentCommentId))).thenReturn(Optional.of(parentComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(expectedReply);

        // Act
        Comment actualReply = commentService.addReply(parentCommentId, userId, content);

        // Assert
        assertNotNull(actualReply);
        verify(commentRepository, times(1)).findById(eq(parentCommentId));
        verify(commentRepository, times(2)).save(any(Comment.class));
    }

    @Test
    void addReply_ParentCommentNotFound() {
        // Arrange
        String parentCommentId = "nonexistentParentId";
        String userId = "user123";
        String content = "Reply content";

        when(commentRepository.findById(eq(parentCommentId))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.addReply(parentCommentId, userId, content);
        });

        verify(commentRepository, times(1)).findById(eq(parentCommentId));
    }

    @Test
    void addReply_Exception() {
        // Arrange
        String parentCommentId = "parent123";
        String userId = "user123";
        String content = "Reply content";

        Comment parentComment = Comment.builder()
                .commentId(parentCommentId)
                .userId("parentUser")
                .content("Parent comment")
                .build();

        when(commentRepository.findById(eq(parentCommentId))).thenReturn(Optional.of(parentComment));
        when(commentRepository.save(any(Comment.class))).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.addReply(parentCommentId, userId, content);
        });

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void likeComment_Success() {
        // Arrange
        String commentId = "comment123";
        String userId = "user123";

        Comment comment = Comment.builder()
                .commentId(commentId)
                .userId("otherUser")
                .content("Test comment")
                .build();

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        commentService.likeComment(commentId, userId);

        // Assert
        assertTrue(comment.getLikes().stream().anyMatch(ld -> ld.getUserId().equals(userId)));
        verify(commentRepository, times(1)).findById(eq(commentId));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void likeComment_CommentNotFound() {
        // Arrange
        String commentId = "nonexistentCommentId";
        String userId = "user123";

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentNotFoundException.class, () -> {
            commentService.likeComment(commentId, userId);
        });

        verify(commentRepository, times(1)).findById(eq(commentId));
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void likeComment_Exception() {
        // Arrange
        String commentId = COMMENT_ID;
        String userId = USER1;

        Comment comment = Comment.builder()
                .commentId(commentId)
                .userId(USER2)
                .content(COMMENT_CONTENT)
                .build();

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.likeComment(commentId, userId);
        });

        verify(commentRepository, times(1)).findById(eq(commentId));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void dislikeComment_Success() {
        // Arrange
        String commentId = COMMENT_ID;
        String userId = USER1;

        Comment comment = Comment.builder()
                .commentId(commentId)
                .userId(USER2)
                .content(COMMENT_CONTENT)
                .build();

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        commentService.dislikeComment(commentId, userId);

        // Assert
        assertTrue(comment.getDislikes().stream().anyMatch(ld -> ld.getUserId().equals(userId)));
        verify(commentRepository, times(1)).findById(eq(commentId));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void dislikeComment_CommentNotFound() {
        // Arrange
        String commentId = NON_EXISTING_COMMENT_ID;
        String userId = USER1;

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentNotFoundException.class, () -> {
            commentService.dislikeComment(commentId, userId);
        });

        verify(commentRepository, times(1)).findById(eq(commentId));
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void dislikeComment_Exception() {
        // Arrange
        String commentId = COMMENT_ID;
        String userId = USER1;

        Comment comment = Comment.builder()
                .commentId(commentId)
                .userId(USER2)
                .content(COMMENT_CONTENT)
                .build();

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.dislikeComment(commentId, userId);
        });

        verify(commentRepository, times(1)).findById(eq(commentId));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void getReplies_Success() {
        // Arrange
        String commentId = PARENT_COMMENT_ID;
        int n = 5;

        Comment reply1 = Comment.builder().build();
        Comment reply2 = Comment.builder().build();

        List<Comment> expectedReplies = Arrays.asList(reply1, reply2);

        when(commentRepository.findByParentCommentId(eq(commentId), any())).thenReturn(expectedReplies);

        // Act
        List<Comment> actualReplies = commentService.getReplies(commentId, 0, n);

        // Assert
        assertNotNull(actualReplies);
        assertEquals(expectedReplies.size(), actualReplies.size());
        verify(commentRepository, times(1)).findByParentCommentId(eq(commentId), any());
    }

    @Test
    void getReplies_Exception() {
        // Arrange
        String commentId = PARENT_COMMENT_ID;
        int n = 5;

        when(commentRepository.findByParentCommentId(eq(commentId), any())).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.getReplies(commentId, 0, n);
        });

        verify(commentRepository, times(1)).findByParentCommentId(eq(commentId), any());
    }

    @Test
    void getFirstLevelComments_Success() {
        // Arrange
        String postId = POST_ID;
        int n = 5;

        Comment comment1 = Comment.builder().build();
        Comment comment2 = Comment.builder().build();

        List<Comment> expectedComments = Arrays.asList(comment1, comment2);

        when(commentRepository.findByPostIdAndParentCommentIdIsNull(eq(postId), any())).thenReturn(expectedComments);

        // Act
        List<Comment> actualComments = commentService.getFirstLevelComments(postId, n, n);

        // Assert
        assertNotNull(actualComments);
        assertEquals(expectedComments.size(), actualComments.size());
        verify(commentRepository, times(1)).findByPostIdAndParentCommentIdIsNull(eq(postId), any());
    }

    @Test
    void getFirstLevelComments_Exception() {
        // Arrange
        String postId = POST_ID;
        int n = 5;

        when(commentRepository.findByPostIdAndParentCommentIdIsNull(eq(postId), any())).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.getFirstLevelComments(postId, n, n);
        });

        verify(commentRepository, times(1)).findByPostIdAndParentCommentIdIsNull(eq(postId), any());
    }

    @Test
    void getLikes_Success() {
        // Arrange
        String commentId = COMMENT_ID;

        Comment comment = Comment.builder()
                .commentId(commentId)
                .userId(USER1)
                .content(COMMENT_CONTENT)
                .likes(MockUtil.likeUsers)
                .build();

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(comment));

        // Act
        List<String> actualLikes = commentService.getLikes(commentId);

        // Assert
        assertNotNull(actualLikes);
        assertEquals(comment.getLikes().size(), actualLikes.size());
        verify(commentRepository, times(1)).findById(eq(commentId));
    }

    @Test
    void getLikes_CommentNotFound() {
        // Arrange
        String commentId = NON_EXISTING_COMMENT_ID;

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.getLikes(commentId);
        });

        verify(commentRepository, times(1)).findById(eq(commentId));
    }

    @Test
    void getLikes_Exception() {
        // Arrange
        String commentId = COMMENT_ID;

        when(commentRepository.findById(eq(commentId))).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.getLikes(commentId);
        });

        verify(commentRepository, times(1)).findById(eq(commentId));
    }

    @Test
    void getDislikes_Success() {
        // Arrange
        String commentId = COMMENT_CONTENT;

        Comment comment = Comment.builder()
                .commentId(commentId)
                .userId(USER1)
                .content(COMMENT_CONTENT)
                .dislikes(MockUtil.disLikeUsers)
                .build();

        when(commentRepository.findById(eq(commentId))).thenReturn(Optional.of(comment));

        // Act
        List<String> actualDislikes = commentService.getDislikes(commentId);

        // Assert
        assertNotNull(actualDislikes);
        assertEquals(comment.getDislikes().size(), actualDislikes.size());
        verify(commentRepository, times(1)).findById(eq(commentId));
    }

    @Test
    void getDislikes_CommentNotFound() {

        when(commentRepository.findById(eq(NON_EXISTING_COMMENT_ID))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.getDislikes(NON_EXISTING_COMMENT_ID);
        });

        verify(commentRepository, times(1)).findById(eq(NON_EXISTING_COMMENT_ID));
    }

    @Test
    void getDislikes_Exception() {
        // Arrange

        when(commentRepository.findById(eq(COMMENT_ID))).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(CommentsServiceException.class, () -> {
            commentService.getDislikes(COMMENT_ID);
        });

        verify(commentRepository, times(1)).findById(eq(COMMENT_ID));
    }
}
