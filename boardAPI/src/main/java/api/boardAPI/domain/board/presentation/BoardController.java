package api.boardAPI.domain.board.presentation;

import api.boardAPI.domain.board.presentation.dto.request.BoardCreateRequestDto;
import api.boardAPI.domain.board.presentation.dto.response.BoardResponseDto;
import api.boardAPI.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/write")
    public Long writeBoard(@RequestBody BoardCreateRequestDto requestDto) {
        return boardService.create(requestDto);
    }

    @GetMapping("/findAll")
    public List<BoardResponseDto> allBoard() {
        return boardService.all();
    }

    @GetMapping("/find/{id}")
    public BoardResponseDto detailBoard(@PathVariable("id") Long id) {
        return boardService.detail(id);
    }

    @PutMapping("/find/edit/{id}")
    public Long updateBoard(@PathVariable("id") Long id, @RequestBody BoardCreateRequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    @DeleteMapping("/find/{id}")
    public Long deleteBoard(@PathVariable("id") Long id) {
        return boardService.delete(id);
    }
}
