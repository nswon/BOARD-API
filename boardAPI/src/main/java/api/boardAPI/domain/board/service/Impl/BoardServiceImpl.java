package api.boardAPI.domain.board.service.Impl;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.board.domain.repository.BoardQuerydslRepository;
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
    private final BoardQuerydslRepository boardQuerydslRepository;

    /**
     * 연관관계 편의 메서드로 회원정보를 저장합니다.
     * 게시글을 저장합니다.
     */
    @Transactional
    @Override
    public Long create(BoardCreateRequestDto requestDto) {
        Board board = requestDto.toEntity();
        board.confirmWriter(memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
        boardRepository.save(board);

        return board.getId();
    }

    /**
     * 게시글을 전체조회합니다.
     */
    @Override
    public List<BoardResponseDto> all() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 먼저 게시글이 있는지 확인합니다.
     * 있다면 조회합니다.
     */
    @Override
    public BoardResponseDto detail(Long id) {
        Board board = validateBoardExistence(id);
        return BoardResponseDto.builder()
                .board(board)
                .build();
    }

    /**
     * 먼저 게시글이 있는지 확인합니다.
     * 게시글을 쓴 작성자가 본인인지 확인합니다. 다른 회원이 작성한 게시글은 수정할 수 없습니다.
     * 게시글을 수정합니다.
     */
    @Transactional
    @Override
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = validateBoardExistence(id);
        if(!board.getWriter().getUsername().equals(SecurityUtil.getLoginUserEmail())) {
            throw new BoardException(BoardExceptionType.DIFFERENT_MEMBER_NOT_UPDATE);
        }

        board.update(requestDto.getTitle(), requestDto.getContent());
        return board.getId();
    }

    /**
     * 게시글이 있는지 확인합니다.
     * 게시글의 쓴 작성자가 본인인지 확인합니다. 다른 회원이 작성한 게시글은 수정할 수 없습니다.
     * 게시글을 삭제합니다.
     */
    @Transactional
    @Override
    public Long delete(Long id) {
        Board board = validateBoardExistence(id);
        if(!board.getWriter().getUsername().equals(SecurityUtil.getLoginUserEmail())) {
            throw new BoardException(BoardExceptionType.DIFFERENT_MEMBER_NOT_DELETE);
        }

        boardRepository.deleteById(id);
        return board.getId();
    }

    /**
     * 쿼리메서드를 이용해 검색합니다.
     * %keyword%로 실행됩니다.
     */
    @Override
    public List<BoardResponseDto> search(String keyword) {
        return boardRepository.findByTitleContaining(keyword).stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 페이징을 합니다. 한 페이지당 10개의 게시글이 보입니다.
     */
    @Override
    public Page<BoardResponseDto> paging(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 10);
        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::new);
    }

    /**
     * Querydsl 을 이용해 검색합니다.
     * 제목과 작성자의 닉네임으로 검색할 수 있으며,
     * %title%, nickname 으로 실행됩니다.
     */
    @Override
    public List<BoardResponseDto> findByBoard(String keyword) {
        return boardQuerydslRepository.findBoardByTitleAndWriter(keyword).stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    private Board validateBoardExistence(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));
    }
}
