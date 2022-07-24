package api.boardAPI.domain.comment.presentation.dto.request;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequestDto {

    @NotNull(message = "내용을 입력해주세요.")
    @Size(min = 2, max = 4000)
    private String content;
    private Board board;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .board(board)
                .build();
    }
}
