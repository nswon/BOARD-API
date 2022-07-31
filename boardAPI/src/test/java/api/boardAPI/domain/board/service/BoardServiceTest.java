package api.boardAPI.domain.board.service;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.board.domain.repository.BoardRepository;
import api.boardAPI.domain.board.exception.BoardException;
import api.boardAPI.domain.board.exception.BoardExceptionType;
import api.boardAPI.domain.board.presentation.dto.request.BoardCreateRequestDto;
import api.boardAPI.domain.board.presentation.dto.response.BoardResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    private void clear() {
        em.flush();
        em.clear();
    }

    private BoardCreateRequestDto makeCreateRequestDto() {
        return BoardCreateRequestDto.builder()
                .title(null)
                .content("내용")
                .build();
    }

    //TODO : 토큰 테스트부터 해야함
    @Test
    public void 게시글작성_성공() throws Exception {
        //given
        BoardCreateRequestDto requestDto = makeCreateRequestDto();

        Long boardId = boardService.create(requestDto);
        clear();

        //when
        BoardResponseDto responseDto = boardService.detail(boardId);

        //then
        assertThat(responseDto.getId()).isNotNull();
        assertThat(responseDto.getTitle()).isEqualTo("스프링");
        assertThat(responseDto.getContent()).isEqualTo("내용");
    }

    @Test
    public void 게시글작성_실패_필드입력안함() throws Exception {
        //given
        BoardCreateRequestDto requestDto = makeCreateRequestDto();

        //when

        //then
        Assertions.assertThrows(Exception.class, ()-> boardService.create(requestDto));
    }

}