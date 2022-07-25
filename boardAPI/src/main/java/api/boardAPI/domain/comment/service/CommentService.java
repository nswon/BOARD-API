package api.boardAPI.domain.comment.service;

import api.boardAPI.domain.comment.presentation.dto.request.CommentCreateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.request.CommentUpdateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {
    Long create(Long boardId, CommentCreateRequestDto requestDto);

    Long createReComment(Long boardId, Long parentId, CommentCreateRequestDto requestDto);

    List<CommentResponseDto> all();

    CommentResponseDto detail(Long id);

    Long update(Long id, CommentUpdateRequestDto requestDto);

    Long delete(Long id);
}
