package api.boardAPI.domain.comment.service;

import api.boardAPI.domain.comment.presentation.dto.request.CommentCreateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.request.CommentUpdateRequestDto;

public interface CommentService {
    Long create(Long boardId, CommentCreateRequestDto requestDto);

    Long update(Long id, CommentUpdateRequestDto requestDto);

    Long delete(Long id);
}
