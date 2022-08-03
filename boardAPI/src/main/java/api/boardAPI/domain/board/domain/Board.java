package api.boardAPI.domain.board.domain;

import api.boardAPI.domain.comment.domain.Comment;
import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BOARD")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addBoard(this);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }
}
