package com.intuit.commentsService.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommentRequest extends CommentDTO{
    @NotBlank
    private String postId;
}
