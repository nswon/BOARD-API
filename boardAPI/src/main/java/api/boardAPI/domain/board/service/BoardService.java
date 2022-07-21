package api.boardAPI.domain.board.service;

import api.boardAPI.domain.board.presentation.dto.request.BoardCreateRequestDto;
import api.boardAPI.domain.board.presentation.dto.response.BoardResponseDto;

import java.util.List;

public interface BoardService {
    Long create(BoardCreateRequestDto requestDto);
    List<BoardResponseDto> all();
    BoardResponseDto detail(Long id);
    Long update(Long id, BoardCreateRequestDto requestDto);
    Long delete(Long id);
}
