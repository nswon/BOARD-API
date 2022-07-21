package api.boardAPI.domain.board.domain.repository;

import api.boardAPI.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
