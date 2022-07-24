package api.boardAPI.domain.comment.presentation.dto.request;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequestDto {

    private String content;
    private Board board;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .board(board)
                .build();
    }
}
