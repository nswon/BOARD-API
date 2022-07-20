package api.boardAPI.domain.board.service;

import api.boardAPI.domain.board.presentation.dto.request.BoardCreateRequestDto;
import api.boardAPI.domain.board.presentation.dto.request.BoardUpdateRequestDto;
import api.boardAPI.domain.board.presentation.dto.response.BoardResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    Long createBoard(BoardCreateRequestDto requestDto);
    List<BoardResponseDto> allBoard();
    BoardResponseDto detailBoard(Long id);
    Long updateBoard(Long id, BoardUpdateRequestDto requestDto);
    Long deleteBoard(Long id);
    Page<BoardResponseDto> searchBoard(String keyword);
    Page<BoardResponseDto> pagingBoard(int pageNum);
}
