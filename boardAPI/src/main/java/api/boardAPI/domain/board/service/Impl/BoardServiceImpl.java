package api.boardAPI.domain.board.service.Impl;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.board.domain.repository.BoardRepository;
import api.boardAPI.domain.board.exception.BoardException;
import api.boardAPI.domain.board.exception.BoardExceptionType;
import api.boardAPI.domain.board.presentation.dto.request.BoardCreateRequestDto;
import api.boardAPI.domain.board.presentation.dto.request.BoardUpdateRequestDto;
import api.boardAPI.domain.board.presentation.dto.response.BoardResponseDto;
import api.boardAPI.domain.board.service.BoardService;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.exception.MemberException;
import api.boardAPI.domain.member.exception.MemberExceptionType;
import api.boardAPI.global.security.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Long create(BoardCreateRequestDto requestDto) {
        Board board = requestDto.toEntity();
        board.confirmWriter(memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
        boardRepository.save(board);

        return board.getId();
    }

    @Override
    public List<BoardResponseDto> all() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public BoardResponseDto detail(Long id) {
        Board board = validateBoardExistence(id);
        return BoardResponseDto.builder()
                .board(board)
                .build();
    }

    @Transactional
    @Override
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = validateBoardExistence(id);
        board.update(requestDto.getTitle(), requestDto.getContent());
        return board.getId();
    }

    @Transactional
    @Override
    public Long delete(Long id) {
        Board board = validateBoardExistence(id);
        boardRepository.deleteById(id);
        return board.getId();
    }

    @Override
    public List<BoardResponseDto> search(String keyword) {
        return boardRepository.findByTitleContaining(keyword).stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BoardResponseDto> paging(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 10);
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::new);
    }

    public Board validateBoardExistence(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));
    }
}
