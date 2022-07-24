package api.boardAPI.domain.comment.service.Impl;

import api.boardAPI.domain.board.domain.repository.BoardRepository;
import api.boardAPI.domain.board.exception.BoardException;
import api.boardAPI.domain.board.exception.BoardExceptionType;
import api.boardAPI.domain.comment.domain.Comment;
import api.boardAPI.domain.comment.domain.repository.CommentRepository;
import api.boardAPI.domain.comment.presentation.dto.request.CommentCreateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.request.CommentUpdateRequestDto;
import api.boardAPI.domain.comment.service.CommentService;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.exception.MemberException;
import api.boardAPI.domain.member.exception.MemberExceptionType;
import api.boardAPI.global.security.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Long create(Long boardId, CommentCreateRequestDto requestDto) {
        Comment comment = requestDto.toEntity();
        comment.confirmWriter(memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmBoard(boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD)));

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    @Override
    public Long update(Long id, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUserEmail())) {
            throw new IllegalArgumentException("다른 회원 댓글은 수정할 수 없습니다.");
        }
        comment.updateContent(requestDto.getContent());
        return comment.getId();
    }

    @Transactional
    @Override
    public Long delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUserEmail())) {
            throw new IllegalArgumentException("다른 회원 댓글은 삭제할 수 없습니다.");
        }
        commentRepository.delete(comment);
        return comment.getId();
    }
}
