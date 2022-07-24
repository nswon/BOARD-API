package api.boardAPI.domain.comment.presentation.dto.response;

import api.boardAPI.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private String writer;
    private Long boardId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getWriter().getNickname();
        this.boardId = comment.getBoard().getId();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();
    }

}
