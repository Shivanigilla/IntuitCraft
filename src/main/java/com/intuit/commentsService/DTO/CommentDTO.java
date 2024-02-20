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
public class CommentDTO {
    @NotBlank(message = "Please provide valid userId")
    private String userId;

    @NotBlank(message = "Please provide valid content")
    private String content;
}
