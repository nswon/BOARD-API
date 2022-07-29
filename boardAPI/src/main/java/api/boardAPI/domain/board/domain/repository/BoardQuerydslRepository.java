package api.boardAPI.domain.board.domain.repository;

import api.boardAPI.domain.board.domain.Board;
import static api.boardAPI.domain.board.domain.QBoard.board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BoardQuerydslRepository {

    @PersistenceContext
    EntityManager em;
    private final JPAQueryFactory query;

    public BoardQuerydslRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Board> findByTitle_Querydsl(String keyword) {
        return query
                .selectFrom(board)
                .where(board.title.contains(keyword).or(board.writer.nickname.eq(keyword)))
                .fetch();
    }
}
