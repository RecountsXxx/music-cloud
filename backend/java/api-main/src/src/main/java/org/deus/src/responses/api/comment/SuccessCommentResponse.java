package org.deus.src.responses.api.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.comment.CommentDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessCommentResponse {
    private CommentDTO comment;
}
