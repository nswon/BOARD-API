package api.boardAPI.domain.comment.domain;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "COMMENT")
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean isRemoved = false;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addComment(this);
    }

    public void confirmBoard(Board board) {
        this.board = board;
        board.addComment(this);
    }

    public void confirmParent(Comment parent){
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Comment child){
        childList.add(child);
    }

    //== 수정 ==//
    public void updateContent(String content) {
        this.content = content;
    }

    //== 삭제 ==//
    public void remove() {
        this.isRemoved = true;
    }

    @Builder
    public Comment( Member writer, Board board, Comment parent, String content) {
        this.writer = writer;
        this.board = board;
        this.parent = parent;
        this.content = content;
        this.isRemoved = false;
    }
}
