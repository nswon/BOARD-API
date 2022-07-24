package api.boardAPI.domain.comment.domain.repository;

import api.boardAPI.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
