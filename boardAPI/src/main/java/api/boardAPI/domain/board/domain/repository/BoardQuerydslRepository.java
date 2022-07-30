package api.boardAPI.domain.board.domain.repository;

import api.boardAPI.domain.board.domain.Board;
import static api.boardAPI.domain.board.domain.QBoard.board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Board> findBoardByTitleAndWriter(String keyword) {
        return queryFactory
                .selectFrom(board)
                .where(board.title.contains(keyword).or(board.writer.nickname.eq(keyword)))
                .fetch();
    }
}
