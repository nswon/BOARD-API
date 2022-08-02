package api.boardAPI.domain.comment.service.Impl;

import api.boardAPI.domain.board.domain.repository.BoardRepository;
import api.boardAPI.domain.board.exception.BoardException;
import api.boardAPI.domain.board.exception.BoardExceptionType;
import api.boardAPI.domain.comment.domain.Comment;
import api.boardAPI.domain.comment.domain.repository.CommentRepository;
import api.boardAPI.domain.comment.exception.CommentException;
import api.boardAPI.domain.comment.exception.CommentExceptionType;
import api.boardAPI.domain.comment.presentation.dto.request.CommentCreateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.request.CommentUpdateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.response.CommentResponseDto;
import api.boardAPI.domain.comment.service.CommentService;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.exception.MemberException;
import api.boardAPI.domain.member.exception.MemberExceptionType;
import api.boardAPI.global.security.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 먼저 댓글을 쓸 게시글이 있는지 확인합니다.
     * 연관관계 편의 메서드로 회원정보와 게시글을 저장합니다.
     */
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

    /**
     * 먼저 대댓글을 쓸 게시글과 댓글이 있는지 확인합니다.
     * 연관관계 편의 메서드로 회원정보와 게시글, 부모댓글을 저장합니다.
     */
    @Transactional
    @Override
    public Long createReComment(Long boardId, Long parentId, CommentCreateRequestDto requestDto) {
        Comment comment = requestDto.toEntity();
        comment.confirmWriter(memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmBoard(boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD)));
        comment.confirmParent(validateCommentExistence(parentId));

        commentRepository.save(comment);
        return comment.getId();
    }

    /**
     * 댓글을 전체조회합니다.
     */
    @Override
    public List<CommentResponseDto> all() {
        return commentRepository.findAll().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 먼저 댓글이 있는지 확인합니다.
     * 있다면 조회합니다.
     */
    @Override
    public CommentResponseDto detail(Long id) {
        Comment comment = validateCommentExistence(id);
        return CommentResponseDto.builder()
                .comment(comment)
                .build();
    }

    /**
     * 먼저 댓글이 있는지 확인합니다.
     * 댓글을 쓴 작성자가 본인인지 확인합니다. 다른사람의 댓글은 수정할 수 없습니다.
     * 댓글을 수정합니다.
     */
    @Transactional
    @Override
    public Long update(Long id, CommentUpdateRequestDto requestDto) {
        Comment comment = validateCommentExistence(id);
        if (!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUserEmail())) {
            throw new CommentException(CommentExceptionType.DIFFERENT_MEMBER_NOT_UPDATE);
        }

        comment.updateContent(requestDto.getContent());
        return comment.getId();
    }

    /**
     * 먼저 댓글이 있는지 확인합니다.
     * 댓글을 쓴 작성자가 본인인지 확인합니다. 다른사람의 댓글은 삭제할 수 없습니다.
     * 댓글을 삭제합니다. 부모댓글 삭제 시 자식 댓글도 삭제됩니다.
     */
    @Transactional
    @Override
    public Long delete(Long id) {
        Comment comment = validateCommentExistence(id);
        if (!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUserEmail())) {
            throw new CommentException(CommentExceptionType.DIFFERENT_MEMBER_NOT_DELETE);
        }

        commentRepository.delete(comment);
        return comment.getId();
    }

    private Comment validateCommentExistence(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
    }
}
