package api.boardAPI.domain.board.presentation.dto.response;

import api.boardAPI.domain.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private int view;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter().getNickname();
        this.view = board.getView();
        this.createdDate = board.getCreatedDate();
        this.modifiedDate = board.getModifiedDate();
    }
}
