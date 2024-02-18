package com.intuit.commentsService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequest extends CommentDTO{
    @NotBlank
    private String parentCommentId;
}
