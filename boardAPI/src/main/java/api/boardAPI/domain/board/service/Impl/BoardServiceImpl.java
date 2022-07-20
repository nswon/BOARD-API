package api.boardAPI.domain.board.service.Impl;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.board.domain.repository.BoardRepository;
import api.boardAPI.domain.board.presentation.dto.request.BoardCreateRequestDto;
import api.boardAPI.domain.board.presentation.dto.request.BoardUpdateRequestDto;
import api.boardAPI.domain.board.presentation.dto.response.BoardResponseDto;
import api.boardAPI.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public Long createBoard(BoardCreateRequestDto requestDto) {
        log.info("log title = ", requestDto.getTitle());
        log.info("log content = ", requestDto.getContent());
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    @Override
    public List<BoardResponseDto> allBoard() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public BoardResponseDto detailBoard(Long id) {
        return boardRepository.findById(id)
                .map(BoardResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    @Transactional
    @Override
    public Long updateBoard(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        board.update(requestDto.getTitle(), requestDto.getContent());
        return board.getId();
    }

    @Transactional
    @Override
    public Long deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        boardRepository.deleteById(id);
        return  board.getId();
    }

    @Transactional
    @Override
    public Page<BoardResponseDto> searchBoard(String keyword) {
        return new PageImpl<>(boardRepository.findByTitleContaining(keyword)
                .stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList()));
    }

    @Override
    public Page<BoardResponseDto> pagingBoard(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 5);
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::new);
    }

}
