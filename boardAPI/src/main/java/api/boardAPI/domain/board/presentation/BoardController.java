package api.boardAPI.domain.board.presentation;

import api.boardAPI.domain.board.presentation.dto.request.BoardCreateRequestDto;
import api.boardAPI.domain.board.presentation.dto.request.BoardUpdateRequestDto;
import api.boardAPI.domain.board.presentation.dto.response.BoardResponseDto;
import api.boardAPI.domain.board.service.BoardService;
import api.boardAPI.global.wrap.Wrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public Long createBoard(@RequestBody @Valid BoardCreateRequestDto requestDto) {
        return boardService.create(requestDto);
    }

    @GetMapping("/findAll")
    public Page<BoardResponseDto> allBoard(@RequestParam(value = "page", defaultValue = "0") int pageNum) {
        return boardService.paging(pageNum);
    }

    @GetMapping("/find/{id}")
    public BoardResponseDto detailBoard(@PathVariable("id") Long id) {
        return boardService.detail(id);
    }

    @PutMapping("/find/edit/{id}")
    public Long updateBoard(@PathVariable("id") Long id,
                            @RequestBody @Valid BoardUpdateRequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    @DeleteMapping("/find/{id}")
    public Long deleteBoard(@PathVariable("id") Long id) {
        return boardService.delete(id);
    }

    @GetMapping("/search/v1")
    public Wrap searchBoardV1(@RequestParam("keyword") String keyword) {
        return new Wrap(boardService.search(keyword));
    }

    @GetMapping("/search/v2")
    public Wrap searchBoardV2(@RequestParam("keyword") String keyword) {
        return new Wrap(boardService.findByBoard(keyword));
    }
}
