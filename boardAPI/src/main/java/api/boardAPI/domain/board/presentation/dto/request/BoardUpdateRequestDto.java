package api.boardAPI.domain.board.presentation.dto.request;

import api.boardAPI.domain.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdateRequestDto {

    @NotNull(message = "제목을 입력해 주세요.")
    @Size(min = 2, max = 30)
    private String title;

    @NotNull(message = "내용을 입력해 주세요.")
    @Size(min = 2)
    private String content;
}
