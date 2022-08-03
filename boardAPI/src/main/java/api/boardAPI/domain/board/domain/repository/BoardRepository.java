package api.boardAPI.domain.board.domain.repository;

import api.boardAPI.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleContaining(String keyword);
    @Modifying
    @Query("update Board b set b.view = b.view + 1 where b.id = :id")
    int updateView(@Param("id") Long id);
}
