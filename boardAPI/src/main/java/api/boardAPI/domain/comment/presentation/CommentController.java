package api.boardAPI.domain.comment.presentation;

import api.boardAPI.domain.comment.presentation.dto.request.CommentCreateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.request.CommentUpdateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.response.CommentResponseDto;
import api.boardAPI.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public Long createComment(@PathVariable("boardId") Long boardId,
                              @RequestBody @Valid CommentCreateRequestDto requestDto) {
        return commentService.create(boardId, requestDto);
    }

    @PostMapping("/{boardId}/{commentId}")
    public Long createReComment(@PathVariable("boardId") Long boardId,
                                @PathVariable("commentId") Long commentId,
                                @RequestBody @Valid CommentCreateRequestDto requestDto) {
        return commentService.createReComment(boardId, commentId, requestDto);
    }

    @GetMapping("/findAll")
    public List<CommentResponseDto> allComment() {
        return commentService.all();
    }

    @GetMapping("/find/{commentId}")
    public CommentResponseDto detailComment(@PathVariable("commentId") Long commentId) {
        return commentService.detail(commentId);
    }

    @PutMapping("/{commentId}")
    public Long updateComment(@PathVariable("commentId") Long commentId,
                              @RequestBody @Valid CommentUpdateRequestDto requestDto) {
        return commentService.update(commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    public Long deleteComment(@PathVariable("commentId") Long commentId) {
        return commentService.delete(commentId);
    }
}
