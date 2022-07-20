package api.boardAPI.domain.board.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardUpdateRequestDto {

    private String title;
    private String content;

}
