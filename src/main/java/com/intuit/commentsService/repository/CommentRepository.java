package com.intuit.commentsService.repository;

import com.intuit.commentsService.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    Comment save(Comment comment);
    Optional<Comment>  findById(String commentId);
    List<Comment> findByPostIdAndParentCommentIdIsNull(String postId, Pageable pageable);
    List<Comment> findByParentCommentId(String parentCommentId, Pageable pageable);
}

