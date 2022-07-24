package api.boardAPI.domain.comment.presentation;

import api.boardAPI.domain.comment.presentation.dto.request.CommentCreateRequestDto;
import api.boardAPI.domain.comment.presentation.dto.request.CommentUpdateRequestDto;
import api.boardAPI.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public Long createComment(@PathVariable("postId") Long postId,
                              @RequestBody CommentCreateRequestDto requestDto) {
        return commentService.create(postId, requestDto);
    }

    @PutMapping("/{commentId}")
    public Long updateComment(@PathVariable("commentId") Long commentId,
                              @RequestBody CommentUpdateRequestDto requestDto) {
        return commentService.update(commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    public Long deleteComment(@PathVariable("commentId") Long commentId) {
        return commentService.delete(commentId);
    }
}
