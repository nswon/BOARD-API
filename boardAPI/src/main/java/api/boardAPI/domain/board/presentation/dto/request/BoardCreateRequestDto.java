package api.boardAPI.domain.board.presentation.dto.request;

import api.boardAPI.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardCreateRequestDto {

    private String title;
    private String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .build();
    }

    @Builder
    public BoardCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
